# SmartAQI Backend Setup (Login, Register, Admin)

Your HTML pages already call these APIs. Follow these steps to run the Java backend.

## Step 1: MySQL

1. Install MySQL 8.0+ and start the service.
2. Open `backend/src/com/smartaqi/DatabaseConnection.java` and set your password:
   ```java
   private static final String PASSWORD = "your_mysql_password";
   ```
3. Import the database:
   ```bash
   mysql -u root -p < database/schema.sql
   ```

## Step 2: MySQL JDBC Driver

Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) and place the jar in:
```
backend/mysql-connector-j-8.4.0.jar
```

## Step 3: Apache Tomcat 9

1. Download [Tomcat 9](https://tomcat.apache.org/download-90.cgi).
2. Set environment variable (Command Prompt):
   ```bat
   set CATALINA_HOME=C:\path\to\apache-tomcat-9.0.xx
   ```

## Step 4: Compile

```bat
cd c:\Users\sneha\Desktop\smartaqi
compile.bat
```

## Step 5: Deploy

Copy the entire `smartaqi` folder to:
```
%CATALINA_HOME%\webapps\SmartAQI
```

Start Tomcat (`bin\startup.bat`), then open:
```
http://localhost:8080/SmartAQI/
```

---

## API Endpoints (Authentication)

| Action | Method | URL | Body |
|--------|--------|-----|------|
| Register | POST | `/SmartAQI/api/register` | `{"fullname":"...","email":"...","password":"..."}` |
| Login (user/admin) | POST | `/SmartAQI/api/login` | `{"email":"...","password":"..."}` |
| Logout | POST | `/SmartAQI/api/logout` | — |
| Current user | GET | `/SmartAQI/api/user` | (session cookie) |

## Default Admin Login

| Email | Password |
|-------|----------|
| admin@smartaqi.com | admin123 |

Admin uses the same `LoginServlet` — `admin.html` checks that `role === "admin"` before allowing access.

## Test Flow

1. **Register** → `register.html` → creates row in `users` table
2. **User login** → `login.html` → session created → `dashboard.html`
3. **Admin login** → `admin.html` → only `role=admin` allowed → `dashboard.html`

## Troubleshooting

| Problem | Fix |
|---------|-----|
| "Cannot connect to server" | Tomcat not running or app not deployed under `webapps/SmartAQI` |
| "Database error" | Wrong MySQL password or `schema.sql` not imported |
| Login works but dashboard redirects to login | Open app via `http://localhost:8080/SmartAQI/` (not by double-clicking HTML files) |
