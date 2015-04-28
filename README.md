# awesomeFb
Đồ án khoa học web - Học kì 2 năm học 2014-2015

## Nội dung đề tài
Thiết kế và xây dựng công cụ để lấy các thảo luận (comment) từ mạng xã hội Facebook.

### Yêu cầu cơ bản:
- [ ] Thu thập và lưu trữ các nội dung thảo luận theo chủ đề cho trước, mỗi một
thảo luận có các nội dung sau: 
  - [ ] Thông tin người gởi thảo luận (Tên, email, phone, giới tính...)
  - [ ] Nội dung thảo luận
  - [ ] Thời gian gởi thảo luận
  - [ ] Vị trí (location)
  - [ ] Platform (gửi bằng di động, destop,...)
  - [ ] Link gốc của thảo luận
  - [ ] Chủ đề của thảo luận.
- [ ] Truy vấn nội dung dựa theo chủ đề - thời gian: hiển thị các bài thảo luận, thống
kê số lượng thảo luận, số lượng người tham gia thảo luận, giới tính...

### Yêu cầu mở rộng:
- [ ] Lọc các bài thảo luận spam (các thảo luận không liên quan đến chủ đề, hoặc các
thảo luận bị lặp lại nhiều lần).
- [ ] Phân loại các thảo luận: đồng tình/không đồng tình/trung tính với chủ đề tương
ứng (Opinion mining, Sentiment analysis).
- [ ] Thu thập theo chủ đề/điều kiện cho trước (không thông qua API search).

### Yêu cầu về kỹ thuật:
- [ ] Phần thu thập được phát triển dựa trên ngôn ngữ Java (ưu tiên) hoặc C#.
- [ ] Phần truy vấn và hiển thị được phát triển trên các ngôn ngữ/nền tảng tùy ý (Ưu
tiên nền tảng Web).
- [ ] Phần lưu trữ database sử dụng các hệ NoSQL: mongoDB…
- [ ] Các phần phải được thiết kế thành các Module để có thể tái sử dụng.

### Yêu cầu về tài liệu:
- [ ] Báo cáo các kết quả đã đạt được.
- [ ] Tài liệu hướng dẫn sử dụng, cấu hình… (nên kèm video demo).
- [ ] Báo cáo đánh giá điểm mạnh yếu, hướng phát triển tiếp theo.

## Cách đánh giá

|  | Yếu 0-20% | Trung bình 30-50% |  Khá 60-80% | Tốt 90-100% |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| <b>Yêu cầu cơ bản</b> | Chương trình không chạy, có lỗi hoặc không thu thập được dữ liệu | Thu thập được dữ liệu nhưng tốc độ chậm < 20 comment/phút hoặc truy vấn chưa chính xác/chậm ( 1 truy vấn > 1 phút) | Thu thập nhanh dữ liệu nhưng chưa đầy đủ hoặc hiển thị kết quả truy vấn sơ sài | Thu thập nhanh & đầy đủ, hiển thị truy vấn trực quan, logic |
| <b>Yêu cầu nâng cao</b> | Không thực hiện được các yêu cầu nào | Thực hiện được 1 yêu cầu nhưng kết quả thấp (độ chính xác <70%) | Thực hiện được 1 yêu cầu nhưng kết quả thấp (độ chính xác >70%) | Thực hiện được 2 yêu cầu nhưng kết quả thấp (độ chính xác >70%) |
| <b>Yêu cầu về kỹ thuật</b> | Đáp ứng không đúng yêu cầu về ngôn ngữ | Đúng ngôn ngữ nhưng không đáp ứng yêu cầu tái sử dụng | Đáp ứng tốt các yêu cầu kỹ thuật | Sử dụng các công nghê được ưu tiên trong yêu cầu |
| <b>Yêu cầu về tài liệu/báo cáo</b> | Không có báo cáo | Báo cáo mang tính hình thức, không trình bày các nội dung người đọc quan tâm | Báo cáo đầy đủ nhưng trình bày chưa tốt | Báo cáo đầy đủ, trình bày rõ ràng mạch lạc |

## Môi trường lập trình

* IDE: Eclipse Luna.
* Chuẩn lập trình: tuân theo [Google Java Style](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html) (chú ý *Formatting* và *Naming*).

## Các công nghệ sẽ được sử dụng

* [Spring Social Facebook](http://projects.spring.io/spring-social-facebook/): Facebook API dành cho Java.
* [Spring Framework](http://projects.spring.io/spring-framework/): nền tảng server backend.
* [nodejs](http://nodejs.org/): nền tảng server.
* [mongodb](https://www.mongodb.org/): cơ sở dữ liệu noSQL.
* [Semantic UI](http://semantic-ui.com/): framework dùng để thiết kế giao diện.
* ...

## Tài liệu tham khảo

* [Facebook Graph API Doc](https://developers.facebook.com/docs/graph-api)
* [Spring Social Facebook reference](http://docs.spring.io/spring-social-facebook/docs/2.0.1.RELEASE/reference/htmlsingle/)
* ...

## Cách cài đặt project (backend)

Chỉ cần vào folder project và chạy ```gradlew build```

*Cài đặt gradle plugin cho Eclipse nếu cần: https://marketplace.eclipse.org/content/gradle-integration-eclipse-44*

