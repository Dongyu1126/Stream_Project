package com.sangeng;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

    public static void main(String[] args) {
        List<Author> authors = getAuthors();
        //Stream<Author> stream = authors.stream();//test01单列集合转换为stream流
        //test01(authors);

        //test02();//数组转换为stream流
        
        //test03();//双列集合转换为stream流

        //test04();//map的输出类型设计，把对流中的元素进行计算或转换
        //test05();//sorted对流中的元素进行排序  limit设置流的最大长度
        //test06();//skip跳过流中的前n个元素，返回剩下的元素
        //test07();//flatMap可以把一个对象转换成多个对象作为流中的元素,即把多个流对象（比如数组嵌套的情况），变成一个流对象，总结就是万川归海
        //test08();//count可以用来获取当前流中元素的个数。
        //test09();//max min获取最大值和最小值
        //test10();//collect把当前流转换成一个集合。
        //test11();
        //test12();//anyMatch:可以用来判断是否有任意符合匹配条件的元素，结果为boolean类型。
        //test13();//allMatch:可以用来判断是否都符合匹配条件，结果为boolean类型。如果都符合结果为true，否则结果为false。
        //test14();//noneMatch:可以判断流中的元素是否都不符合匹配条件。如果都不符合结果为true，否则结果为false
        //test15();//findAny:获取流中的任意一个元素。该方法没有办法保证获取的一定是流中的第一个元素。
        //test16();//findFirst:获取流中的第一个元素。
        //test17();//reduce归并:把stream中的元素给组合起来，我们可以传入一个初始值，它会按照我们的计算方式依次拿流中的元素和初始化值进行计算，计算结果再和后面的元素计算。
        //test18();//两个参数的reduce 需要设置初始化值，再去处理流中元素
        //test19();//一个参数的reduce 不需要设置初始化值，流中第一个元素即为初始化值
        //test20();//Stream基本类型优化
        test21();//parallel()把串行流转化为并行流,多个线程并行
    }

    private static void test21() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer sum = stream.parallel()//并行  parallelStream可以并行和stream
                .peek(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer num) {
                        System.out.println(num + Thread.currentThread().getName());
                    }
                })
                .filter(num -> num > 5)
                .reduce((res, ele) -> res + ele)
                .get();
        System.out.println(sum);
    }

    private static void test20() {
        List<Author> authors = getAuthors();
//        authors.stream()
//                .map(author -> author.getAge())
//                .map(age -> age + 10) //这样会有多次拆箱和装箱的过程，很消耗资源
//                .filter(age -> age > 18)
//                .map(age -> age+2)
//                .forEach(System.out::println);

        authors.stream()  //后面都是同一种类型，没有拆箱和装箱，效率提高很多
                .mapToInt(new ToIntFunction<Author>() {
                    @Override
                    public int applyAsInt(Author author) {
                        return author.getAge();
                    }
                })
                .map(age -> age + 10)
                .filter(age -> age > 18)
                .map(age -> age+2)
                .forEach(System.out::println);
    }

    private static void test19() {
        //        使用reduce求所有作者中年龄的最小值
        List<Author> authors = getAuthors();
        Optional<Integer> min = authors.stream()
                .map(author -> author.getAge())
                .reduce(new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer element) {
                        return result < element ? result : element;
                    }
                });
        //System.out.println(min);//结果为Optional[14]，格式不太对哈，还得ifPresent
        min.ifPresent(new Consumer<Integer>() {
            @Override
            public void accept(Integer age) {
                System.out.println(age);
            }
        });
    }

    private static void test18() {
        //        使用两个参数的reduce求所有作者中年龄的最大值
        List<Author> authors = getAuthors();
        Integer max = authors.stream()
                .map(author -> author.getAge())
                .reduce(Integer.MIN_VALUE, new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer element) {
                        return result < element ? element : result;
                    }
                });
        System.out.println(max);
    }

    private static void test17() {
        //        使用reduce求所有作者年龄的和
        List<Author> authors = getAuthors();
        Integer sum = authors.stream()
                .distinct()
                .map(author -> author.getAge())//因为reduce里面要初始化的是年龄这种类型，所以先用map转换一下类型，reduce和map二者通常在一起的，让我想到了hadoop里的MapReduce
                .reduce(0, new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer element) {
                        return result + element;
                    }
                });
        System.out.println(sum);
    }

    private static void test16() {
        //        获取一个年龄最小的作家，并输出他的姓名。
        List<Author> authors = getAuthors();
        Optional<Author> first = authors.stream()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o1.getAge() - o2.getAge();
                    }
                }).findFirst();
        first.ifPresent(new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                System.out.println(author.getName());
            }
        });

    }

    private static void test15() {
        //        获取任意一个年龄大于18的作家，如果存在就输出他的名字
        List<Author> authors = getAuthors();
        Optional<Author> optionalAuthor = authors.stream()
                .filter(author -> author.getAge()>18)
                .findAny();
        optionalAuthor.ifPresent(new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                System.out.println(author.getName());
            }
        });
    }

    private static void test14() {
        //        判断作家是否都没有超过100岁的。
        List<Author> authors = getAuthors();

        boolean b = authors.stream()
                .noneMatch(author -> author.getAge() > 100);

        System.out.println(b);
    }

    private static void test13() {
        //        判断是否所有的作家都是成年人
        List<Author> authors = getAuthors();
        boolean flag = authors.stream()
                .allMatch(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getAge() > 10;
                    }
                });
        System.out.println(flag);
    }

    private static void test12() {
        //        判断是否有年龄在29以上的作家
        List<Author> authors = getAuthors();
        boolean flag = authors.stream()
                .anyMatch(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getAge() > 29;
                    }
                });
        System.out.println(flag);
    }

    private static void test11() {
        //        获取一个Map集合，map的key为作者名，value为List<Book>
        List<Author> authors = getAuthors();
        Map<String, List<Book>> map = authors.stream()
                .distinct()
                .collect(Collectors.toMap(new Function<Author, String>() {
                    @Override
                    public String apply(Author author) {
                        return author.getName();
                    }
                }, new Function<Author, List<Book>>() {
                    @Override
                    public List<Book> apply(Author author) {
                        return author.getBooks();
                    }
                }));
        System.out.println(map);
    }

    private static void test10() {
        //获取一个所有书名的Set集合。
        List<Author> authors = getAuthors();
        Set<List<Book>> bookset = authors.stream()
                .map(author -> author.getBooks())
                .collect(Collectors.toSet());
        System.out.println(bookset);


    }

    private static void test09() {
        //        分别获取这些作家的所出书籍的最高分和最低分并打印。
        //思路：Stream<Author>  -> Stream<Book> ->Stream<Integer>  ->求值
        List<Author> authors = getAuthors();
        Optional<Integer> max = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> book.getScore())
                .max((score1, score2) -> score1 - score2);//max是Optional所以要返回一个值，并且不是stream流所以不能再“.min”

        Optional<Integer> min = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> book.getScore())
                .min((score1, score2) -> score1 - score2);//max是Optional所以要返回一个值，并且不是stream流所以不能再“.min”

        System.out.println(max.get());
        System.out.println(min.get());
    }

    private static void test08() {
        //        打印这些作家的所出书籍的数目，注意删除重复元素。
        List<Author> authors = getAuthors();
        long count = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .count();//count方法必须要有返回值
        System.out.println(count);
    }

    private static void test07() {
        //        打印现有数据的所有分类。要求对分类进行去重。不能出现这种格式：哲学,爱情     爱情
        List<Author> authors = getAuthors();
        authors.stream()
                .flatMap(author -> author.getBooks().stream())//拿到所有书籍的集合，拼接所有作家的书籍stream流变成单独的stream流
                .distinct()//给数据去重
                .flatMap(book ->  Arrays.stream(book.getCategory().split(",")))//book.getCategory().split(",")为字符串数组，再次把它变成流
                .distinct()//对书籍类型进行去重
                .forEach(category -> System.out.println(category));
    }

    private static void test06() {
        List<Author> authors = getAuthors();
        authors.stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o1.getAge()-o2.getAge();
                    }
                }).skip(1)//跳过第一个，只返回其他几个
                .forEach(new Consumer<Author>() {
                    @Override
                    public void accept(Author author) {
                        System.out.println(author.getAge());
                    }
                });
    }

    private static void test05() {
        List<Author> authors = getAuthors();
        //        对流中的元素按照年龄进行降序排序，并且要求不能有重复的元素。
        authors.stream()
                .distinct()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o2.getAge()-o1.getAge();
                    }
                })
                .limit(2)//限制只输出2个
                .forEach(new Consumer<Author>() {
                    @Override
                    public void accept(Author author) {
                        System.out.println(author.getAge());
                    }
                });
    }

    private static void test04() {
        List<Author> authors = getAuthors();
        authors.stream()
                .map(new Function<Author, String>() {//设置输入类型为Author输出类型为String，把对流中的元素进行计算或转换，这里只输出作家姓名
                    @Override
                    public String apply(Author author) {
                        return author.getName();
                    }
                }).distinct().forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                });
    }

    private static void test03() {
        Map<String,Integer> map = new HashMap<>();
        map.put("蜡笔小新",19);
        map.put("黑子",17);
        map.put("日向翔阳",16);

        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        Stream<Map.Entry<String, Integer>> stream = entrySet.stream();

        stream.filter(new Predicate<Map.Entry<String, Integer>>() {
            @Override
            public boolean test(Map.Entry<String, Integer> entry) {
                return entry.getValue()>16;
            }
        }).forEach(entry -> System.out.println(entry.getKey()+":"+entry.getValue()));
    }

    private static void test02() {
        Integer[] arr = {1,2,3,4,5};
        //Stream<Integer> stream = Arrays.stream(arr); //方法1
        Stream<Integer> stream = Stream.of(arr);//方法2
        stream.distinct()
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        return integer > 2;
                    }
                })
                        .forEach(integer -> System.out.println(integer));
    }

    private static void test01(List<Author> authors) {//ctrl+alt+m可以单独抽取出来这个方法  test01单列集合
        authors.stream()
                .distinct()
                .filter(author -> author.getAge() < 18)
                .forEach(author -> System.out.println(author.getName()));
    }

    private static List<Author> getAuthors() {
        //数据初始化
        Author author = new Author(1L,"蒙多",33,"一个从菜刀中明悟哲理的祖安人",null);
        Author author2 = new Author(2L,"亚拉索",15,"狂风也追逐不上他的思考速度",null);
        Author author3 = new Author(3L,"易",14,"是这个世界在限制他的思维",null);
        Author author4 = new Author(3L,"易",14,"是这个世界在限制他的思维",null);

        //书籍列表
        List<Book> books1 = new ArrayList<>();
        List<Book> books2 = new ArrayList<>();
        List<Book> books3 = new ArrayList<>();

        books1.add(new Book(1L,"刀的两侧是光明与黑暗","哲学,爱情",88,"用一把刀划分了爱恨"));
        books1.add(new Book(2L,"一个人不能死在同一把刀下","个人成长,爱情",99,"讲述如何从失败中明悟真理"));

        books2.add(new Book(3L,"那风吹不到的地方","哲学",85,"带你用思维去领略世界的尽头"));
        books2.add(new Book(3L,"那风吹不到的地方","哲学",85,"带你用思维去领略世界的尽头"));
        books2.add(new Book(4L,"吹或不吹","爱情,个人传记",56,"一个哲学家的恋爱观注定很难把他所在的时代理解"));

        books3.add(new Book(5L,"你的剑就是我的剑","爱情",56,"无法想象一个武者能对他的伴侣这么的宽容"));
        books3.add(new Book(6L,"风与剑","个人传记",100,"两个哲学家灵魂和肉体的碰撞会激起怎么样的火花呢？"));
        books3.add(new Book(6L,"风与剑","个人传记",100,"两个哲学家灵魂和肉体的碰撞会激起怎么样的火花呢？"));

        author.setBooks(books1);
        author2.setBooks(books2);
        author3.setBooks(books3);
        author4.setBooks(books3);

        List<Author> authorList = new ArrayList<>(Arrays.asList(author,author2,author3,author4));
        return authorList;
    }
}
