# 🌐 WebCharity

> Dự án Website từ thiện được phát triển bằng J2EE (Java Enterprise Edition).  
> Mục tiêu là hỗ trợ quản lý và tổ chức các chiến dịch quyên góp, kết nối nhà tài trợ với người cần giúp đỡ.

---

## 🚀 Tính năng chính

- Đăng ký / Đăng nhập người dùng
- Quản lý chiến dịch từ thiện
- Quản lý người nhận và nhà tài trợ
- Thống kê và quản lý giao dịch
- Giao diện quản trị cho admin

---

## 💻 Công nghệ sử dụng

- Java J2EE (Servlet, JSP)
- Apache Tomcat
- JDBC + MySQL
- HTML/CSS/JS
- Git + GitHub

---

## 📥 Cách tải code về máy

### 🔧 Yêu cầu trước:
- Đã cài:
  - [Java JDK 8+](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
  - [Apache Tomcat](https://tomcat.apache.org/)
  - [MySQL](https://www.mysql.com/) + MySQL Workbench
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/) hoặc Eclipse
  - Git

### 📦 Bước 1: Clone repository

```bash
git clone https://github.com/TanTai08/WebCharity.git
Hoặc tải về file ZIP:

Truy cập: https://github.com/TanTai08/WebCharity

Nhấn nút Code > Download ZIP

Giải nén và mở trong IDE

🛠 Bước 2: Import project vào IntelliJ hoặc Eclipse
Chọn Open Project hoặc Import > Existing project

Chọn thư mục WebCharity

Cấu hình Tomcat và MySQL nếu cần

🗄 Bước 3: Tạo CSDL MySQL
sql
Sao chép
Chỉnh sửa
CREATE DATABASE webcharity;
-- Import file script.sql nếu có
▶️ Bước 4: Chạy ứng dụng
Chạy trên Tomcat server

Truy cập: http://localhost:8080/WebCharity
