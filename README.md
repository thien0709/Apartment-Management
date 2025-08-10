# 🏢 Apartment Management System

> A comprehensive property management solution designed to streamline administrative operations, tenant relations, and facility maintenance workflows. The system is built using the MVC architecture, integrates real-time messaging, and supports payments via VNPay.

---

## ✨ Features

### 🏠 Client Features
- **User authentication and profile management**  
  (login, view/update profile & avatar, change password)
- **View invoices**
- **Pay via VNPay or bank transfer** for service fees *(rent, water, management, etc.)*
- **Submit, view, and edit feedback**
- **View and participate in surveys**
- **Create vehicle card for relatives**
- **Real-time chat with administrator**

### 🔧 Admin Management Features
- **Admin account and role management**
- **User management** *(accounts, roles, permissions)*
- **Locker management**
- **Service fee management**
- **Statistics and reporting**


---

## 🚀 Tech Stack

<div align="center">

| **Backend**          | **Frontend**          | **Database**             | **Tools & Services**       |
|----------------------|-----------------------|--------------------------|----------------------------|
| ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) | ![ReactJS](https://img.shields.io/badge/ReactJS-20232A?style=for-the-badge&logo=react&logoColor=61DAFB) | ![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white) | ![Firebase](https://img.shields.io/badge/Firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black) |
| Spring MVC           | JavaScript ES6+        | JPA / Hibernate          | JWT Authentication          |
| VNPay                | HTML5 & CSS3           | Firebase Realtime Database| Thymeleaf Templates         |

</div>



---

## 🛠 Installation Guide

### 📋 **Prerequisites**
- ☕ Java 11 or higher
- 🗄️ MySQL 8.0+
- 📦 Node.js 16.x+
- 🐈 Git

### 🔧 **Backend Setup**

1️⃣ **Clone Repository**
```bash
git clone https://github.com/thien0709/Apartment-Management.git
cd Apartment-Management-BE
```

2️⃣ **Database Configuration**
```sql
-- Create database
CREATE DATABASE apartment_management;
USE apartment_management;
```

📥 **[Download SQL Script](database/apartment-management.sql)** and execute in your MySQL client

### 3️⃣ Environment Configuration

Configure all required settings (MySQL, Cloudinary, VNPAY, Firebase, SMTP) in the file:  
`src/main/resources/application.properties`

> Make sure to replace placeholder values with your actual credentials and keep them secure.


4️⃣ **Build & Run**
```bash
# Install dependencies
mvn clean install

# Start application
mvn spring-boot:run

# Server will run on: http://localhost:8080
```

### 🎨 **Frontend Setup**

1️⃣ **Clone & Navigate**
```bash
git clone https://github.com/thien0709/Apartment-Management-FE.git
cd Apartment-Management-FE
```

2️⃣ **Install & Start**
```bash
# Install dependencies
npm install

# Start development server
npm start

# Application will run on: http://localhost:3000
```

---

## 📁 Project Structure

```
Directory structure:
└── thien0709-apartment-management/
    ├── README.md
    ├── Apartment-Management-BE/
    │   ├── nb-configuration.xml
    │   ├── pom.xml
    │   └── src/
    │       └── main/
    │           ├── java/
    │           │   └── com/
    │           │       ├── apartment_management/
    │           │       │   ├── configs/
    │           │       │   │   ├── DispatcherServletInit.java
    │           │       │   │   ├── HibernateConfigs.java
    │           │       │   │   ├── SecurityWebApplicationInitializer.java
    │           │       │   │   ├── SpringSecurityConfigs.java
    │           │       │   │   ├── ThymeleafConfig.java
    │           │       │   │   ├── VNPayConfig.java
    │           │       │   │   └── WebAppContextConfigs.java
    │           │       │   ├── controllers/
    │           │       │   │   ├── ApiCardControllers.java
    │           │       │   │   ├── ApiChatController.java
    │           │       │   │   ├── ApiFeedbackController.java
    │           │       │   │   ├── ApiInvoiceControllers.java
    │           │       │   │   ├── ApiLockersController.java
    │           │       │   │   ├── ApiPaymentsController.java
    │           │       │   │   ├── ApisCardController.java
    │           │       │   │   ├── ApiSurveysController.java
    │           │       │   │   ├── ApiUserControllers.java
    │           │       │   │   ├── CardController.java
    │           │       │   │   ├── ChatController.java
    │           │       │   │   ├── FeedbackController.java
    │           │       │   │   ├── IndexController.java
    │           │       │   │   ├── InvoiceController.java
    │           │       │   │   ├── LockerController.java
    │           │       │   │   ├── ResponseFBController.java
    │           │       │   │   ├── StatisticsController.java
    │           │       │   │   ├── SurveyController.java
    │           │       │   │   ├── UserController.java
    │           │       │   │   └── VNPayController.java
    │           │       │   ├── dto/
    │           │       │   │   ├── reponse/
    │           │       │   │   │   ├── AnswerResponse.java
    │           │       │   │   │   ├── FeedbackResponse.java
    │           │       │   │   │   └── UserReponse.java
    │           │       │   │   └── request/
    │           │       │   │       └── PaymentRequest.java
    │           │       │   ├── filtes/
    │           │       │   │   └── JwtFilter.java
    │           │       │   ├── pojo/
    │           │       │   │   ├── Card.java
    │           │       │   │   ├── DetailInvoice.java
    │           │       │   │   ├── Feed.java
    │           │       │   │   ├── Feedback.java
    │           │       │   │   ├── Floor.java
    │           │       │   │   ├── Invoice.java
    │           │       │   │   ├── Locker.java
    │           │       │   │   ├── Package.java
    │           │       │   │   ├── Question.java
    │           │       │   │   ├── Response.java
    │           │       │   │   ├── Room.java
    │           │       │   │   ├── Survey.java
    │           │       │   │   └── User.java
    │           │       │   ├── repositories/
    │           │       │   │   ├── CardRepository.java
    │           │       │   │   ├── DetailInvoiceRepository.java
    │           │       │   │   ├── FeedBackRepository.java
    │           │       │   │   ├── FeedRepository.java
    │           │       │   │   ├── FloorRepository.java
    │           │       │   │   ├── InvoiceRepository.java
    │           │       │   │   ├── LockerRepository.java
    │           │       │   │   ├── PackageRepository.java
    │           │       │   │   ├── QuestionRepository.java
    │           │       │   │   ├── ResponseRepository.java
    │           │       │   │   ├── RoomRepository.java
    │           │       │   │   ├── SurveyRepository.java
    │           │       │   │   ├── UserRepository.java
    │           │       │   │   └── impl/
    │           │       │   │       ├── CardRepositoryImpl.java
    │           │       │   │       ├── DetailInvoiceRepositoryImpl.java
    │           │       │   │       ├── FeedBackRepositoryImpl.java
    │           │       │   │       ├── FeedRepositoryImpl.java
    │           │       │   │       ├── FloorRepositoryImpl.java
    │           │       │   │       ├── InvoiceRepositoryImpl.java
    │           │       │   │       ├── LockerRepositoryImpl.java
    │           │       │   │       ├── PackageRepositoryImpl.java
    │           │       │   │       ├── QuestionRepositoryImpl.java
    │           │       │   │       ├── ResponseRepositoryImpl.java
    │           │       │   │       ├── RoomRepositoryImpl.java
    │           │       │   │       ├── SurveyRepositoryImpl.java
    │           │       │   │       └── UserRepositoryImpl.java
    │           │       │   ├── services/
    │           │       │   │   ├── CardService.java
    │           │       │   │   ├── DetailInvoiceService.java
    │           │       │   │   ├── EmailService.java
    │           │       │   │   ├── FeedBackService.java
    │           │       │   │   ├── FeedService.java
    │           │       │   │   ├── FloorService.java
    │           │       │   │   ├── InvoiceService.java
    │           │       │   │   ├── LockerService.java
    │           │       │   │   ├── PackageService.java
    │           │       │   │   ├── QuestionService.java
    │           │       │   │   ├── ResponseService.java
    │           │       │   │   ├── RoomService.java
    │           │       │   │   ├── SurveyService.java
    │           │       │   │   ├── UserService.java
    │           │       │   │   ├── VNPayService.java
    │           │       │   │   └── impl/
    │           │       │   │       ├── CardServiceImpl.java
    │           │       │   │       ├── DetailInvoiceServiceImpl.java
    │           │       │   │       ├── EmailServiceImpl.java
    │           │       │   │       ├── FeedBackServiceImpl.java
    │           │       │   │       ├── FeedServiceImpl.java
    │           │       │   │       ├── FloorServiceImpl.java
    │           │       │   │       ├── InvoiceServiceImpl.java
    │           │       │   │       ├── LockerServiceImpl.java
    │           │       │   │       ├── PackageServiceImpl.java
    │           │       │   │       ├── QuestionServiceImpl.java
    │           │       │   │       ├── ResponseServiceImpl.java
    │           │       │   │       ├── RoomServiceImpl.java
    │           │       │   │       ├── SurveyServiceImpl.java
    │           │       │   │       └── UserServiceImpl.java
    │           │       │   └── utils/
    │           │       │       └── JwtUtils.java
    │           │       └── lt/
    │           │           └── apartment_management/
    │           │               ├── JakartaRestConfiguration.java
    │           │               └── resources/
    │           │                   └── JakartaEE11Resource.java
    │           ├── resources/
    │           │   ├── application.properties
    │           │   ├── messages.properties
    │           │   ├── messages_vi.properties
    │           │   ├── META-INF/
    │           │   │   └── persistence.xml
    │           │   └── templates/
    │           │       ├── accout.html
    │           │       ├── base.html
    │           │       ├── cards.html
    │           │       ├── chat_admin.html
    │           │       ├── create-invoice.html
    │           │       ├── feedback_list.html
    │           │       ├── fragments.html
    │           │       ├── index.html
    │           │       ├── invoice-list.html
    │           │       ├── login.html
    │           │       ├── manage_locker.html
    │           │       ├── manage_user.html
    │           │       ├── register.html
    │           │       ├── statistics.html
    │           │       ├── survey_detail.html
    │           │       ├── survey_list.html
    │           │       └── survey_question_answers.html
    │           └── webapp/
    │               ├── META-INF/
    │               │   └── context.xml
    │               └── WEB-INF/
    │                   ├── applicationContext.xml
    │                   ├── beans.xml
    │                   └── web.xml
    └── Apartment-Management-FE/
        ├── README.md
        ├── package.json
        ├── public/
        │   ├── index.html
        │   ├── manifest.json
        │   └── robots.txt
        └── src/
            ├── App.css
            ├── App.js
            ├── App.test.js
            ├── index.css
            ├── index.js
            ├── reportWebVitals.js
            ├── setupTests.js
            ├── components/
            │   ├── card.js
            │   ├── changePassword.js
            │   ├── chat.js
            │   ├── feedback.js
            │   ├── home.js
            │   ├── login.js
            │   ├── payment.js
            │   ├── surveyDetail.js
            │   ├── surveyList.js
            │   ├── uploadAvatar.js
            │   ├── layout/
            │   │   ├── carousel.js
            │   │   ├── footer.js
            │   │   ├── header.js
            │   │   ├── sidebar.js
            │   │   └── spinner.js
            │   └── styles/
            │       ├── login.css
            │       └── payment.css
            └── configs/
                ├── Apis.js
                ├── firebase.js
                └── MyContexts.js

```

---

## 🌐 API Endpoints for Client

| Method   | Endpoint                          | Description                     |
|----------|---------------------------------|---------------------------------|
| POST     | `/login`                        | User login                     |
| GET      | `/admins`                       | Get list of admins             |
| GET      | `/secure/profile`               | Get current user profile       |
| PUT      | `/users/{userId}/change_password` | Change user password          |
| PUT      | `/users/{userId}/update_avatar` | Update user avatar             |
| GET      | `/invoices/{userId}`            | Get user's invoices            |
| POST     | `/payment/vnpay`                | VNPay payment                  |
| POST     | `/payment/banking`              | Banking payment                |
| POST     | `/feedback`                    | Submit feedback                |
| GET      | `/feedback/{userId}`            | Get feedbacks by user          |
| PUT      | `/feedback/{feedbackId}`        | Edit feedback                  |
| GET      | `/surveys`                     | Get list of surveys            |
| GET      | `/survey/{surveyId}`            | Get survey detail              |
| POST     | `/card/create`                 | Create new card                |
| DELETE   | `/card/delete/{cardId}`         | Delete card                   |

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. 🍴 Fork the repository
2. 🌿 Create feature branch: `git checkout -b feature/amazing-feature`
3. 💾 Commit changes: `git commit -m 'Add amazing feature'`
4. 📤 Push to branch: `git push origin feature/amazing-feature`
5. 🔄 Open Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Bach Xuan Thien**
- 📧 Email: thien070904@gmail.com
- 🐙 GitHub: [@thien0709](https://github.com/thien0709)
- 📍 Location: Ho Chi Minh City, Vietnam

---

<div align="center">

### ⭐ Don't forget to star this repository if you found it helpful!

</div>