package com.sangeng;

import java.util.Optional;
import java.util.function.Consumer;

public class OptionalDemo {
    public static void main(String[] args) {
       // Author author = getAuthor();
//        if(author!=null){
//            System.out.println(author.getName());
//        }
//        Optional<Author> authorOptional = Optional.ofNullable(author);//推荐使用此方法获取值，为空不为空都适用
//        authorOptional.ifPresent(author1 -> System.out.println(author1.getName()));
        Optional<Author> authorOptional = getAuthorOptional();//推荐使用此方法获取值，为空不为空都适用
        authorOptional.ifPresent(author2 -> System.out.println(author2.getName()));

    }
    public static Optional<Author> getAuthorOptional(){//静态方法配置好Optional直接调用
        Author author = new Author(1L,"蒙多",33,"一个从菜刀中明悟哲理的祖安人",null);
        return Optional.ofNullable(author);//ofNullable允许你将一个可能为 null 的值包装在一个 Optional 对象中，并且可以在后续的操作中处理这个值，避免了空指针异常。
    }
    public static Author getAuthor(){//任然需要单独调用Optional
        Author author = new Author(1L,"蒙多",33,"一个从菜刀中明悟哲理的祖安人",null);
        return author;
    }
}
