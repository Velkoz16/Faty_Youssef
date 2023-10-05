package com.framework_creation.utilities;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

public class LibraryAPI_Util {

    private static String token ;
    public String getToken(){
        return token;
    }


    /**
     * Return TOKEN as String by using provided username from /token endpoint
     * @param userType
     * @return
     */
    public static String getToken(String userType){


        String email=ConfigurationReader.getProperty(userType+"_username");
        String password="libraryUser";



        String token = getToken(email,password);

        return token;
    }

    public static String getToken(String email,String password){


        return given()
                .contentType(ContentType.URLENC)
                .formParam("email" , email)
                .formParam("password" , password).
                when()
                .post(ConfigurationReader.getProperty("library.baseUri")+"/login")
                .prettyPeek()
                .path("token") ;


    }

    public static RequestSpecification requestSpecificationWithAuth(String userType){
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("x-library-token",getToken(userType));

        return requestSpecification;
    }

    public static Map<String,Object> getRandomBookMap(){

        Faker faker = new Faker() ;
        Map<String,Object> bookMap = new LinkedHashMap<>();
        String randomBookName = faker.book().title() + faker.number().numberBetween(0, 10);
        bookMap.put("name", randomBookName);
        bookMap.put("isbn", faker.code().isbn10()   );
        bookMap.put("year", faker.number().numberBetween(1000,2021)   );
        bookMap.put("author",faker.book().author()   );
        bookMap.put("book_category_id", faker.number().numberBetween(1,20)   );  // in library app valid category_id is 1-20
        bookMap.put("description", faker.chuckNorris().fact() );

        return bookMap ;
    }

    public static Map<String,Object> getRandomUserMap(){
        String password = "libraryUser";

        Faker faker = new Faker() ;
        Map<String,Object> bookMap = new LinkedHashMap<>();
        String fullName = faker.name().fullName();
        String email=fullName.substring(0,fullName.indexOf(" "))+"@library";
        System.out.println(email);
        bookMap.put("full_name", fullName );
        bookMap.put("email", email);
        bookMap.put("password",password );
        // 2 is librarian as role
        bookMap.put("user_group_id",2);
        bookMap.put("status", "ACTIVE");
        bookMap.put("start_date", "2023-03-11");
        bookMap.put("end_date", "2024-03-11");
        bookMap.put("address", faker.address().cityName());

        return bookMap ;
    }

}
