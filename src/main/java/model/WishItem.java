package model;

import javax.swing.*;

public class WishItem {

        private int id;
        private String itemName;
        private double price;
        private ImageIcon photoIcon;
        private String path;

        // конструктор service
        public WishItem(String name, double price, String path) {
            this.itemName = name;
            this.price = price;
            this.photoIcon = new ImageIcon(path);
            this.path = path;
        }
        // panel
        public WishItem(String name, double price, ImageIcon photoIcon, String path) {
            this.itemName = name;
            this.price = price;
            this.photoIcon = photoIcon;
            this.path = path;
        }

        // геттеры
        public String getItemName() {
            return itemName;
        }

        public double getPrice() {
            return price;
        }

        public ImageIcon getPhotoIcon() {
            return photoIcon;
        }
        public String getPath() {
            return path;
        }
        public void setPath(String path) {
            this.path = path;
        }
    }

