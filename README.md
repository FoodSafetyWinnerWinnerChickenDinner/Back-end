# Back-end

## 오늘 뭐 먹지?
각 영양소의 일일 섭취 권장량을 기준으로 사용자의 매 끼니를 책임져 줄 메뉴 추천 서비스입니다.
<br><br>
**영양소 항목** <br>
+ 총 열량 (Kcal) <br>
+ 탄수화물 (Carbohydrate), 단백질 (Protein), 지방 (Fat) <br>
+ 당류 (Sugar), 나트륨 (Sodium) <br>
+ 포화지방산 (Saturated fatty acid), 콜레스테롤 (Cholesterol), 트랜스지방 (Trans fat)<br>

<br><br>
## Version env
> - Springboot 2.3.4
> - Java(IntelliJ) version :  IntelliJ (2020.2.1)
> - Java(TM) SE Runtime Environment (build 1.8.0_91-b14)


<br><br>
## Operating Process
1. Client로부터 사용자가 현재 먹은 음식의 정보를 받습니다. **Map 형식 <음식 이름(String), n 인분(Integer)>**
2. 영양소들의 권장 섭취량을 **g 단위**로 변환합니다. <br>
3. 변환한 g 단위 정보와 사용자의 섭취량을 비교합니다.
4. 부족한 영양소 기반으로 식약처의 [영양성분DB](https://www.foodsafetykorea.go.kr/api/newDatasetDetail.do)를 토대로 추천된 메뉴 리스트를 전달합니다.

<br> 아래의 링크에서 변환한 영양소에 대한 정보를 참고해 주세요. <br>
[Notion for nutrients detail](https://www.notion.so/9d87a9a1cc57486eb826a02f6d265ee9?v=0916fc5531fc44f3a8a33522275155f2)

<br><br>
## Architecture
> 진행중


<br><br>
## License
[Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0)

<br><br>
## Contact
> - e-mail : **minuk8932@naver.com**
> - github : **https://github.com/FoodSafetyWinnerWinnerChickenDinner/Back-end/issues**
