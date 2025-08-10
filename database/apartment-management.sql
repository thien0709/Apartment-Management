CREATE DATABASE apartment_management;
USE apartment_management;

-- 1. Bảng tầng
CREATE TABLE floor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    floor_number INT NOT NULL
);

-- 2. Bảng phòng
CREATE TABLE room (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL,
    floor_id INT,
    max_people INT NOT NULL,
    head_id INT UNIQUE, -- trưởng phòng
    FOREIGN KEY (floor_id) REFERENCES floor(id),
    FOREIGN KEY (head_id) REFERENCES user(id) ON DELETE SET NULL
);

-- 3. Bảng người dùng 
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    avatar_url TEXT,
    role ENUM('ADMIN', 'RESIDENT') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    room_id INT, -- chỉ dùng nếu role = 'RESIDENT'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES room(id)
);
-- 4. Bảng người thân
CREATE TABLE relative (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL, -- user mà người thân này liên kết tới
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    relationship ENUM('PARENT', 'SIBLING', 'CHILD', 'SPOUSE', 'OTHER') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 5. Bảng phản ánh (feedback)
CREATE TABLE feedback (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'RESOLVED') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 6. Bảng hóa đơn
CREATE TABLE invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    issued_date DATE NOT NULL,
    due_date DATE,
    payment_method ENUM('TRANSFER', 'CASH'),
    payment_proof TEXT,
    total_amount DECIMAL(10,2) NOT NULL,
    is_accept BOOLEAN NOT NULL,
    is_active BOOLEAN NOT NULL,
    status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
);

-- 7. Bảng loại phí
CREATE TABLE feed (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT
);

-- 8. Bảng chi tiết hóa đơn
CREATE TABLE detail_invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    feed_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    note TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (feed_id) REFERENCES feed(id) ON DELETE CASCADE,
    UNIQUE (invoice_id, feed_id)
);

-- 9. Bảng tủ đồ
CREATE TABLE locker (
    id INT PRIMARY KEY, -- id trùng với user.id
    FOREIGN KEY (id) REFERENCES user(id)
);

-- 10. Bảng món hàng
CREATE TABLE package (
    id INT AUTO_INCREMENT PRIMARY KEY,
    locker_id INT,
    name VARCHAR(100),
    status ENUM('PENDING', 'RECEIVED') DEFAULT 'PENDING',
    image TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (locker_id) REFERENCES locker(id)
);

-- 11. Bảng thẻ xe/người thân
CREATE TABLE card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,         -- user chịu trách nhiệm thẻ này (người đăng ký thẻ)
    relative_id INT NULL,         -- nếu thẻ cấp cho người thân
    issue_date DATE,
    expiration_date DATE,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (relative_id) REFERENCES relative(id) ON DELETE SET NULL
);

-- 12. Bảng khảo sát
CREATE TABLE survey (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT,
    title VARCHAR(255),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    type ENUM('MULTIPLE_CHOICE', 'SHORT_ANSWER') NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES user(id)
);

-- 13. Câu hỏi khảo sát
CREATE TABLE question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    survey_id INT,
    content TEXT NOT NULL,
    FOREIGN KEY (survey_id) REFERENCES survey(id)
);
-- 14. Bảng chứa các câu hỏi trắc nghiệm
CREATE TABLE question_option (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    content VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- 15. Câu trả lời cư dân
CREATE TABLE response (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    user_id INT NOT NULL,
    option_id INT NULL,          -- Dùng nếu là trắc nghiệm
    answer TEXT NULL,            -- Dùng nếu là trả lời ngắn
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES question_option(id) ON DELETE SET NULL
);
