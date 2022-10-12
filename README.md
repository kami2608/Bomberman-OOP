# *Mô tả về các đối tượng trong trò chơi*

  - ![image](https://user-images.githubusercontent.com/54837897/195387000-1fc9e83a-acad-4d21-a405-9849d8df15ed.png)
Bomber là nhân vật chính của trò chơi. Bomber có thể di chuyển theo 4 hướng trái/phải/lên/xuống theo sự điều khiển của người chơi.
  - ![image](https://user-images.githubusercontent.com/54837897/195387028-253b98ec-d87f-4497-81b5-071eddc59076.png)
Enemy là các đối tượng mà Bomber phải tiêu diệt hết để có thể qua Level. Enemy có thể di chuyển ngẫu nhiên hoặc tự đuổi theo Bomber tùy theo loại Enemy. Các loại Enemy sẽ được mô tả cụ thể ở phần dưới.
  - ![image](https://user-images.githubusercontent.com/54837897/195387069-3dc5ed5e-0219-497f-b040-147bf9fdda63.png)
Bomb là đối tượng mà Bomber sẽ đặt và kích hoạt tại các ô Grass. Khi đã được kích hoạt, Bomber và Enemy không thể di chuyển vào vị trí Bomb. Tuy nhiên ngay khi Bomber vừa đặt và kích hoạt Bomb tại ví trí của mình, Bomber có một lần được đi từ vị trí đặt Bomb ra vị trí bên cạnh. Sau khi kích hoạt 2s, Bomb sẽ tự nổ, các đối tượng Flame được tạo ra.
  - ![image](https://user-images.githubusercontent.com/54837897/195387115-8bb66ef8-81bb-4f3f-a860-7e8709301e3c.png)
Grass là đối tượng mà Bomber và Enemy có thể di chuyển xuyên qua, và cho phép đặt Bomb lên vị trí của nó
  - ![image](https://user-images.githubusercontent.com/54837897/195387167-68ed4ed1-2639-413f-bc96-cb1f6397da9a.png)
Wall là đối tượng cố định, không thể phá hủy bằng Bomb cũng như không thể đặt Bomb lên được, Bomber và Enemy không thể di chuyển vào đối tượng này
  - ![image](https://user-images.githubusercontent.com/54837897/195387194-141f1ac6-028d-4ea8-bc86-089f864b66d4.png)
Brick là đối tượng được đặt lên các ô Grass, không cho phép đặt Bomb lên nhưng có thể bị phá hủy bởi Bomb được đặt gần đó. Bomber và Enemy thông thường không thể di chuyển vào vị trí Brick khi nó chưa bị phá hủy.
  - ![image](https://user-images.githubusercontent.com/54837897/195387248-449d34ba-3855-4ac6-af33-88edce49ed57.png)
Portal là đối tượng được giấu phía sau một đối tượng Brick. Khi Brick đó bị phá hủy, Portal sẽ hiện ra và nếu tất cả Enemy đã bị tiêu diệt thì người chơi có thể qua Level khác bằng cách di chuyển vào vị trí của Portal.

 *Các Item cũng được giấu phía sau Brick và chỉ hiện ra khi Brick bị phá hủy. Bomber có thể sử dụng Item bằng cách di chuyển vào vị trí của Item.*
## Thông tin về các Item:

  - ![image](https://user-images.githubusercontent.com/54837897/195387297-c7609ffc-f4c9-4e2a-a052-1d24a1cefb86.png)
SpeedItem Khi sử dụng Item này, Bomber sẽ được tăng vận tốc di chuyển thêm một giá trị thích hợp
  - ![image](https://user-images.githubusercontent.com/54837897/195387327-243c2036-f7d3-431e-8bab-50f13ba5c8df.png)
FlameItem Item này giúp tăng phạm vi ảnh hưởng của Bomb khi nổ (độ dài các Flame lớn hơn)
  - ![image](https://user-images.githubusercontent.com/54837897/195387353-8f41653f-9988-4cc3-8213-980b4a0a028b.png)
BombItem Thông thường, nếu không có đối tượng Bomb nào đang trong trạng thái kích hoạt, Bomber sẽ được đặt và kích hoạt duy nhất một đối tượng Bomb. Item này giúp tăng số lượng Bomb có thể đặt thêm một.

## Các loại enemy
  - ![image](https://user-images.githubusercontent.com/54837897/195387389-9c9203a2-c542-48a8-9a43-ba2b7210e28b.png)
Balloom là Enemy đơn giản nhất, di chuyển ngẫu nhiên với vận tốc cố định
  - ![image](https://user-images.githubusercontent.com/54837897/195387418-ac8da415-4f9f-4931-953e-16814db7519a.png)
Oneal có tốc độ di chuyển thay đổi, lúc nhanh, lúc chậm và di chuyển "thông minh" hơn so với Balloom (biết đuổi theo Bomber)
  - ![image](https://user-images.githubusercontent.com/54837897/195387475-3cd56b24-99a7-48ad-86e6-bb9e5fe905c3.png)
Pass di chuyển ngẫu nhiên với tốc độ khá nhanh. Khi bị tiêu diệt sẽ sinh ra thêm 2 Ballom
  - ![image](https://user-images.githubusercontent.com/54837897/195387533-076f0d4d-0d35-402d-8fe2-a09c834e5cab.png)
Dahl có tốc độ di chuyển thay đổi, lúc nhanh, lúc chậm
  - ![image](https://user-images.githubusercontent.com/54837897/195387577-5b4ff500-0cd3-42de-8c08-ee18fefdee17.png)
Doria biết đuổi Bomber khi lại gần nhưng phạm vi rộng hơn Oneal, có tốc độ di chuyển tăng và có thể di chuyển xuyên Brick trong khi đuổi Bomber
  - ![image](https://user-images.githubusercontent.com/54837897/195387684-9b463b42-6614-4b9f-b4d5-77fd57bbae9a.png)
Minvo xuất hiện sau khi portal xuất hiện, di chuyển như Oneal

# *Giải thích ký hiệu trong các file level*
  - 1 : nền vàng
  - w : Wall
  - b : Brick
  - 0 : Grass
