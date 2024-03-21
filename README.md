0. Разобрать код с семинара
1. Повторить код с семинара без подглядываний на таблице Student с полями:
   1. id - int
   2. firstName - string
   3. secondName - string
   4. age - int
2. *Попробовать подключиться к другой БД
3. **Придумать, как подружить запросы и reflection:
   1. Создать аннотации Table, Id, Column
   2. Создать класс, у которого есть методы:
      1. save(Object obj) сохраняет объект в БД
      2. update(Object obj) обновляет объект в БД
      3. Попробовать объединить save и update (сначала select, потом update или insert)


```
  @Table(name = "person")
  static class Person {
  @Id
  @Column(name = "id")
  private int id;
  @Column(name = "name")
  private String name;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
```