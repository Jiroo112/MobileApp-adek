    package com.alphatz.adek.Model;

    import android.os.Parcel;
    import android.os.Parcelable;

    public class ResepModel implements Parcelable {
        private int id;
        private String namaMenu;
        private int kalori;
        private byte[] gambar; // For storing image as byte array

        public ResepModel() {}

        public ResepModel(int id, String namaMenu, int kalori, byte[] gambar) {
            this.id = id;
            this.namaMenu = namaMenu;
            this.kalori = kalori;
            this.gambar = gambar;
        }

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNamaMenu() { return namaMenu; }
        public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }

        public int getKalori() { return kalori; }
        public void setKalori(int kalori) { this.kalori = kalori; }

        public byte[] getGambar() { return gambar; }
        public void setGambar(byte[] gambar) { this.gambar = gambar; }

        // Parcelable implementation
        protected ResepModel(Parcel in) {
            id = in.readInt();
            namaMenu = in.readString();
            kalori = in.readInt();

            int gambarLength = in.readInt();
            if (gambarLength > 0) {
                gambar = new byte[gambarLength];
                in.readByteArray(gambar);
            }
        }

        public static final Creator<ResepModel> CREATOR = new Creator<ResepModel>() {
            @Override
            public ResepModel createFromParcel(Parcel in) {
                return new ResepModel(in);
            }

            @Override
            public ResepModel[] newArray(int size) {
                return new ResepModel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(namaMenu);
            dest.writeInt(kalori);

            if (gambar != null) {
                dest.writeInt(gambar.length);
                dest.writeByteArray(gambar);
            } else {
                dest.writeInt(0);
            }
        }
    }