# 📚 ỨNG DỤNG MƯỢN SÁCH THƯ VIỆN

Ứng dụng Android hỗ trợ người dùng tra cứu, đặt lịch và mượn sách trực tuyến tại thư viện. Hệ thống được phát triển với backend Spring Boot và cơ sở dữ liệu MySQL, tích hợp lưu trữ đám mây và trải nghiệm người dùng hiện đại.

---

## ✅ Tính năng nổi bật

- 🔐 Đăng ký / đăng nhập bằng email và OTP
- 🔁 Lấy lại mật khẩu qua OTP
- 🔍 Tìm kiếm và lọc sách theo tên
- 🎧 Nghe thử sách nói trực tiếp từ ứng dụng
- 🕘 Xem lịch sử mượn sách và **gia hạn mượn**
- 📝 Đánh giá, nhận xét sách
- 💖 Thêm sách yêu thích vào **wishlist**
- 📅 Đặt lịch mượn sách trực tuyến
- 👤 Chỉnh sửa hồ sơ cá nhân, cập nhật ảnh đại diện

---

## 🏗️ Kiến trúc hệ thống


---

## 🧑‍💻 Công nghệ sử dụng

| Thành phần         | Công nghệ                       |
|--------------------|----------------------------------|
| Ứng dụng Android   | Java + Android Studio            |
| Backend API        | Spring Boot (Java)               |
| Cơ sở dữ liệu      | MySQL                            |
| Lưu trữ ảnh        | Cloudinary                       |
| Lưu trữ âm thanh   | Supabase                         |
| Kiểu giao tiếp     | RESTful API (JSON)               |
| UI                 | RecyclerView, Fragment           |

---

---
### 🎨 Thiết kế giao diện (Figma)

Bạn có thể xem toàn bộ thiết kế của ứng dụng tại đây

https://www.figma.com/design/loSBaiWZJyNaiaA4lWqfb1/LibraryApp?t=RkPxRcGFxvRkzh9R-0

---

## 🖥️ Hướng dẫn cài đặt

### 1. Backend (Spring Boot)

Truy cập https://github.com/phamdanhhuong/libraryapi để cài đặt api.


### 2. Android App
Mở project bằng Android Studio
Vào đường dẫn sau app/src/main/java/com/phamhuong/library/model/RetrofitClient.java
thay ip của máy chạy backend vào
```java
retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.3:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
```
Chạy ứng dụng trên thiết bị/emulator.
---
### 📌 Định hướng phát triển

-📲 Gợi ý sách theo thói quen người dùng

-🤖 Tích hợp chatbot hỗ trợ thủ thư ảo

-🔗 Thanh toán phí phạt

-🌐 Phát triển phiên bản Web cho thủ thư quản trị

---
### 👨‍💻 Nhóm phát triển
| Họ tên          | MSSV                  | Nhiệm vụ chính                                |
| --------------- | --------------------- | --------------------------------------------- |
| Phạm Danh Hưởng | 22110344              | Đăng nhập/ký, lấy mật khẩu, profile, wishlist |
| Lê Đăng Hiếu    | 22110322              | Tìm kiếm, sách nói, lịch sử, nhận xét, UI     |
| GVHD            | ThS. Nguyễn Hữu Trung | Hướng dẫn kỹ thuật & phản biện                |
