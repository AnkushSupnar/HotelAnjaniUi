package com.anjani.view;

import java.util.ResourceBundle;

public enum FxmlView {
    LOGIN {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/home/Login.fxml";
        }
    },
    HOME {
        @Override
        String getTitle() {
            return "Home Page";
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/home/Home.fxml";
        }
    },
    KIRANA {
        @Override
        String getTitle() {
            return "Kirana Page";
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/transaction/Kirana.fxml";
        }
    },
    PARTY {
        @Override
        String getTitle() {
            return "Add Purchase Party";
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/create/CreatePurchaseParty.fxml";
        }
    },
    BILLING {
        @Override
        String getTitle() {
            return "Daily Billing                                Developed By Ankush 9960857742";
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/transaction/Billing.fxml";
        }
    };
    abstract String getTitle();
    public abstract String getFxmlFile();
    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
