package gui;

import model.WishItem;
import service.WishListService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WishlistPanel extends JPanel {

        private JButton addButton;// кнопка добавления
        private JButton buyButton;// кнопка удаления
        private JPanel itemsPanel;// панель для добавленных вещей
        private JPanel previewPanel;// панель диалогового окна
        private JTextField itemField;// поле для названия вещи
        private JTextField priceField;// поле для цены
        private String itemName;// переменная для вещи
        private String priceText;// переменная для введенной цены
        private double price;// переменная цены
        private ImageIcon photoIcon = null;// фото вещи
        private List<WishItem> wishList;// список всех вещей
        private final WishListService wishListService = new WishListService();
        private String path;// ссылка на изображение

        public WishlistPanel() {

                setLayout(new BorderLayout());

                wishList = new ArrayList<>();// создаем список

                // заголовок
                JLabel wishlistLabel = new JLabel("Мой Wishlist");
                wishlistLabel.setHorizontalAlignment(SwingConstants.CENTER);
                wishlistLabel.setFont(new Font("Arial", Font.BOLD, 20));
                add(wishlistLabel, BorderLayout.NORTH);

                //создаем нижнюю панель
                JPanel lowerPanel = new JPanel();
                lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

                // создаем кнопку добавления
                addButton = new JButton("Добавить вещь в wish-list");
                lowerPanel.add(addButton);
                addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                lowerPanel.add(Box.createVerticalStrut(30));// делаем отступ

                add(lowerPanel, BorderLayout.SOUTH);

                // панель для списка вещей
                itemsPanel = new JPanel();
                itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

                // прокрутка
                JScrollPane scrollPane = new JScrollPane(itemsPanel);
                add(scrollPane, BorderLayout.CENTER);

                // Создаём текстовые поля один раз
                itemField = new JTextField();
                priceField = new JTextField();

                // обработчик кнопки добавить
                addButton.addActionListener(e -> dialogWindow());

                loadWishListFromDataBase();
        }
        private void dialogWindow() {

                // сосзаем поля ввода
                itemField = new JTextField();
                priceField = new JTextField();

                // добавляем их
                JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
                inputPanel.add(new Label("Название: "));
                inputPanel.add(itemField);
                inputPanel.add(new Label("Цена: "));
                inputPanel.add(priceField);

                // метод ля отображения окна с кнопакми
                int result = JOptionPane.showConfirmDialog(
                        this,// компонент над которым появляется диалог
                        inputPanel,// объяект который показан внутри окна
                        "Добавить вещь",// заголовок окна
                        JOptionPane.OK_CANCEL_OPTION,// тип кнопок которые будут в окне
                        JOptionPane.PLAIN_MESSAGE);// тип иконки

                // если пользователь нажимает ОК
                if (result == JOptionPane.OK_OPTION) {

                        // добавляем на панель название и цену
                        itemName = itemField.getText().trim();
                        priceText = priceField.getText().trim();

                        try {
                                price = Double.parseDouble(priceText);
                        } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Цена должна быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                                return;
                        }

                        // Окно с вариантом добавить фото вещи
                        int photoChoice = JOptionPane.showConfirmDialog(
                                this,
                                "Хотите добавить фото вещи?",
                                "Добавление фото",
                                JOptionPane.OK_CANCEL_OPTION
                        );
                        if (photoChoice == JOptionPane.OK_OPTION) {
                                JFileChooser fileChooser = new JFileChooser();//объект который помогает пользователю выбрать файл на диске
                                int fileResult = fileChooser.showOpenDialog(this);// окно выбора файла и метод возвращает код OK_CANCEL_OPTION

                                if (fileResult == JFileChooser.APPROVE_OPTION) {// проверяем нажал ли пользователь ОК
                                        File file = fileChooser.getSelectedFile();// путь к файлу
                                        String path = file.getAbsolutePath();// ссылка для БД
                                        photoIcon = new ImageIcon(path);
                                        photoIcon = new ImageIcon(file.getAbsolutePath());// картинка которую можно вставить в JLable
                                }
                        }else {photoIcon = null; path = null;}
                }

                // переводим текст цены в число
                double price = Double.parseDouble(priceText);
                //создаем лист с названием ценой и фото
                WishItem wishItem = new WishItem(itemName, price, photoIcon, path);
                //устанавливаем путь фото
                wishItem.setPath(path);
                // добавляем wishItem в wishList
                wishList.add(wishItem);
                //сохраняем все в БД
                System.out.println(itemName);
                System.out.println(price);
                System.out.println("Сохраняемый путь: " + wishItem.getPath());
                wishListService.saveWishListService(wishItem);
                // создаем панель для этой вещи
                previewPanel = createPreviewPanel(wishItem);
                // в панель для добавленных вещей добавляем панель previewPanel
                itemsPanel.add(previewPanel);
                // обновляем панель
                itemsPanel.revalidate();
                // перерисовываем ее
                itemsPanel.repaint();
        }
         // метод визуализирует панели и имеет кнопку удаления
        private JPanel createPreviewPanel(WishItem item) {
                // создаем панель
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createLineBorder(Color.PINK, 1));
                panel.setBackground(Color.WHITE);
                panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                panel.add(new JLabel("Название: " + item.getItemName()));
                panel.add(new JLabel("Цена: " + item.getPrice()));

                // добавляем фото
                if (item.getPhotoIcon() != null) {
                        // получаем оригинальное изображение
                        Image originalImage = item.getPhotoIcon().getImage();
                        // устанавливаем размер
                        Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);// создаем ImageIcon уменьшенного размера
                        JLabel photoLabel = new JLabel(scaledIcon);
                        panel.add(photoLabel);
                }

                // добавляем кнопку удаления
                buyButton = new JButton("Куплено");
                panel.setLayout(new FlowLayout(FlowLayout.CENTER));
                panel.add(buyButton);

                // обрабатываем кнопку
                buyButton.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                this,// ссылка на панель
                                "Удалить вещь \"" + item.getItemName() + "\" из wish-list?",
                                "Подтверждение удаления",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                                wishList.remove(item);
                                itemsPanel.remove(panel);
                                wishListService.deleteWishList(item);
                                itemsPanel.revalidate();
                                itemsPanel.repaint();
                        }
                });
                return panel;
        }
        private void loadWishListFromDataBase() {
                wishList = wishListService.loadWishListService();
                itemsPanel.removeAll();

                for (WishItem item : wishList) {
                        String name = item.getItemName();
                        double price = item.getPrice();
                        String path = item.getPath();

                        // Загружаем изображение
                        ImageIcon icon = null;
                        if (path != null && !path.isEmpty()) {
                                icon = new ImageIcon(path);
                                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                                icon = new ImageIcon(img);
                        }

                        // Создаём текстовый лейбл
                        String labelText = "<html><b>" + name + "</b><br>Цена: " + price + "</html>";
                        JLabel label = new JLabel(labelText, icon, JLabel.LEFT);

                        // Кнопка "Куплено"
                        JButton buyButton = new JButton("Куплено");

                        // Панель для одного элемента списка
                        JPanel itemPanel = new JPanel(new BorderLayout());
                        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        itemPanel.add(label, BorderLayout.CENTER);
                        itemPanel.add(buyButton, BorderLayout.EAST);

                        // обрабатываем кнопку
                        buyButton.addActionListener(e -> {
                                int confirm = JOptionPane.showConfirmDialog(
                                        this,// ссылка на панель
                                        "Удалить вещь \"" + item.getItemName() + "\" из wish-list?",
                                        "Подтверждение удаления",
                                        JOptionPane.YES_NO_OPTION
                                );
                                if (confirm == JOptionPane.YES_OPTION) {
                                        wishList.remove(item);
                                        itemsPanel.remove(itemPanel);
                                        wishListService.deleteWishList(item);
                                        itemsPanel.revalidate();
                                        itemsPanel.repaint();
                                }
                        });

                        itemsPanel.add(itemPanel);
                }

                itemsPanel.revalidate();
                itemsPanel.repaint();
        }

}

