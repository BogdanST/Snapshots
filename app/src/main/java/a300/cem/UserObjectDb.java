package a300.cem;

public class UserObjectDb {
        private String uid;
        private String email;
        private String name;
        private String profile;

        public UserObjectDb(String uid, String email, String name, String profile){
            this.uid = uid;
            this.email = email;
            this.name = name;
            this.profile = "default";
        }

        public String getUid(){
            return uid;
        }

        public void setUid(String uid){
            this.uid = uid;
        }

        public String getEmail(){
            return email;
        }
        public void setEmail(String email){
            this.email = email;
        }

        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }

        public String getProfile(){return profile;}
        public void setProfile(String profile){ this.profile = "default";}

}

