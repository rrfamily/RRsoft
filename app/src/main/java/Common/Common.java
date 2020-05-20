package Common;

import Model.user;

public class Common {

    public static user currentUser;


        public static String convertCodeToStatus(String status) {
            switch (status) {
                case "0":
                    return "Placed";
                case "1":
                    return "On Shipping";
                default:
                    return "Shipped";
            }
        }

    }

