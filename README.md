## رزرواسیون کلاس (بکند)
تلاشی جهت پیاده سازی بخش بکند از پروژه درس مهندسی نرم افزار

### پیکربندی
```shell
> git clone https://git.iut.ac.ir/very-soft-project/class-reservation-backend.git
> docker build -t app .
> docker run -dp 8080:8080 app
```

پس از اجرای سرویس، داکیومنت‌ها از [اینجا](http://localhost:8080/swagger-ui/index.html) قابل دسترسی هستند.

### سناریو استفاده
1. User registers with /signup endpoint and specifies his username, password and role
2. User receives the jwt token from /login
3. User can use /managers, /teachers and /students endpoints based on the given token
4. User left the application by /logout