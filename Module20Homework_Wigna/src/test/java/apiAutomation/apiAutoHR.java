package apiAutomation;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class apiAutoHR {

    //menggunakan reqres.in

    //GET

    @Test
    public void getListUserPositive(){

        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("per_page", Matchers.equalTo(6))
                .assertThat().body("total", Matchers.equalTo(12))
                .assertThat().body("total_pages", Matchers.equalTo(2));
    }

    @Test
    public void getListUserNegative1(){

        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(2)) //wrong value
                .assertThat().body("per_page", Matchers.equalTo(6))
                .assertThat().body("total", Matchers.equalTo(12))
                .assertThat().body("total_pages", Matchers.equalTo(2));
    }

    @Test
    public void getListUserNegative2(){

        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("per_page", Matchers.equalTo("Sembilan")) //wrong data type, input String
                .assertThat().body("total", Matchers.equalTo(12))
                .assertThat().body("total_pages", Matchers.equalTo(2));
    }

    @Test
    public void getListUserNegative3(){

        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("per_page", Matchers.equalTo(6))
                .assertThat().body("total", Matchers.equalTo(20)) //wrong value
                .assertThat().body("total_pages", Matchers.equalTo(2));
    }

    @Test
    public void getListUserNegative4(){

        given()
                .when()
                .get("https://reqres.in/api/users?page=999090") //invalid path query
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("per_page", Matchers.equalTo(6))
                .assertThat().body("total", Matchers.equalTo(12))
                .assertThat().body("total_pages", Matchers.equalTo(2));
    }


    //POST

    @Test
    public void postUserPositive(){
        String namaInput = "Emma";
        String jobInput = "Teacher";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", namaInput);
        bodyObj.put("job", jobInput);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(namaInput))
                .assertThat().body("job", Matchers.equalTo(jobInput));
    }

    @Test
    public void postUserNegative1(){
        String namaInput = "Emma";
        String jobInput = "Teacher";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", jobInput);//wrong mapping
        bodyObj.put("job", namaInput); //wrong mapping

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(namaInput)) //this will go wrong
                .assertThat().body("job", Matchers.equalTo(jobInput)); //this will go wrong
    }

    @Test
    public void postUserNegative2(){
        String namaInput = "Emma";
        String jobInput = "Teacher";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", namaInput);
        bodyObj.put("job", jobInput);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api") //incomplete path
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(namaInput))
                .assertThat().body("job", Matchers.equalTo(jobInput));
    }


    @Test
    public void postUserNegative3(){
        String namaInput = "Emma";
        String jobInput = "Teacher";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", namaInput);
        bodyObj.put("job", jobInput);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(400) //wrong status code
                .assertThat().body("name", Matchers.equalTo(namaInput))
                .assertThat().body("job", Matchers.equalTo(jobInput));
    }

    @Test
    public void postUserNegative4(){
        String namaInput = "Emma";
        String jobInput = "Teacher";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", namaInput);
        bodyObj.put("job", jobInput);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("age", Matchers.equalTo(namaInput)) //wrong path. "age" (int) -> "name"
                .assertThat().body("job", Matchers.equalTo(jobInput));
    }


    //PUT

    @Test
    public void putUserPositive(){

        int user_id = 3;
        String newInputName = "Muhammad";

        String fname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.last_name");
        String email = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.email");
        String avatar = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.avatar");

        System.out.println("This is the previous name:" + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put ("id", user_id);
        bodyMap.put ("email", email);
        bodyMap.put ("first_name", newInputName);
        bodyMap.put ("last_name", lname);
        bodyMap.put ("avatar", avatar);

        JSONObject jsonObject = new JSONObject (bodyMap);

        RestAssured.given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newInputName));

    }

    @Test
    public void putUserNegative1(){

        int user_id = 99999776; //invalid userid
        String newInputName = "Muhammad";

        String fname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.last_name");
        String email = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.email");
        String avatar = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.avatar");

        System.out.println("This is the previous name:" + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put ("id", user_id);
        bodyMap.put ("email", email);
        bodyMap.put ("first_name", newInputName);
        bodyMap.put ("last_name", lname);
        bodyMap.put ("avatar", avatar);

        JSONObject jsonObject = new JSONObject (bodyMap);

        RestAssured.given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newInputName));

    }


    @Test
    public void putUserNegative2(){

        int user_id = 3;
        String newInputName = "Muhammad";

        String fname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.last_name");
        String email = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.email");
        String avatar = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.avatar");

        System.out.println("This is the previous name:" + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put ("id", user_id);
        bodyMap.put ("email", newInputName); //wrong mapping
        bodyMap.put ("first_name", email); //wrong mapping
        bodyMap.put ("last_name", lname);
        bodyMap.put ("avatar", avatar);

        JSONObject jsonObject = new JSONObject (bodyMap);

        RestAssured.given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newInputName));

    }

    @Test
    public void putUserNegative3(){

        int user_id = 3;
        String newInputName = "Muhammad";

        String fname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.last_name");
        String email = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.email");
        String avatar = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.avatar");

        System.out.println("This is the previous name:" + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put ("id", user_id);
        bodyMap.put ("email", email);
        bodyMap.put ("first_name", newInputName);
        bodyMap.put ("last_name", lname);
        bodyMap.put ("avatar", avatar);

        JSONObject jsonObject = new JSONObject (bodyMap);

        RestAssured.given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("avatar", Matchers.equalTo(newInputName)); //wrong path. "avatar"

    }

    @Test
    public void putUserNegative4(){

        int user_id = 3;
        String newInputName = "Muhammad";

        String fname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.last_name");
        String email = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.email");
        String avatar = given().when().get("https://reqres.in/api/users/"+user_id).getBody().jsonPath().get("data.avatar");

        System.out.println("This is the previous name:" + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put ("id", user_id);
        bodyMap.put ("email", email);
        bodyMap.put ("first_name", newInputName);
        bodyMap.put ("last_name", lname);
        bodyMap.put ("avatar", avatar);

        JSONObject jsonObject = new JSONObject (bodyMap);

        RestAssured.given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("https://reqres.in/"+user_id) //incomplete path URI
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newInputName));

    }


    //PATCH

    @Test
    public void patchUserPositive(){
        int user_id = 2;

        String newInputLastName = "Fakhry";
        String lname = given().when().get("https://reqres.in/api/users/" + user_id).getBody().jsonPath().get("data.last_name");

        System.out.println("This is the previous last name: " + lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", user_id);
        bodyMap.put("last_name", newInputLastName);

        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo(newInputLastName));
    }

    @Test
    public void patchUserNegative1(){
        String user_id = "Dua"; //wrong data type. Should be int -> String

        String newInputLastName = "Fakhry";
        String lname = given().when().get("https://reqres.in/api/users/" + user_id).getBody().jsonPath().get("data.last_name");

        System.out.println("This is the previous last name: " + lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", user_id);
        bodyMap.put("last_name", newInputLastName);

        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo(newInputLastName));
    }



    @Test
    public void patchUserNegative2(){
        int user_id = 4;

        String newInputLastName = "Fakhry";
        String lname = given().when().get("https://reqres.in/api/users/" + user_id).getBody().jsonPath().get("data.last_name");

        System.out.println("This is the previous last name: " + lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", user_id);
        bodyMap.put("last_name", user_id); //wrong Object mapping

        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo(newInputLastName));
    }

    @Test
    public void patchUserNegative3(){
        int user_id = 4;

        String newInputLastName = "Fakhry";
        String lname = given().when().get("https://reqres.in/api/users/" + user_id).getBody().jsonPath().get("data.last_name");

        System.out.println("This is the previous last name: " + lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", user_id);
        bodyMap.put("last_name", newInputLastName);

        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo(user_id)); //wrong Matchers EqualTo
    }

    @Test
    public void patchUserNegative4(){
        int user_id = 4;

        String newInputLastName = "Fakhry";
        String lname = given().when().get("https://reqres.in/api/users/" + user_id).getBody().jsonPath().get("data.last_name");

        System.out.println("This is the previous last name: " + lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", user_id);
        bodyMap.put("last_name", newInputLastName);

        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo("Bino")); //using String as equalTo but wrong value
    }

    @Test
    public void patchUserNegative5(){
        int user_id = 4;

        String newInputLastName = "Fakhry";
        String lname = given().when().get("https://reqres.in/api/users/" + user_id).getBody().jsonPath().get("data.last_name");

        System.out.println("This is the previous last name: " + lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", user_id);
        bodyMap.put("last_name", newInputLastName);

        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("https://reqres.in/api/users/"+user_id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo("Fakhry")); //edge case
                                                            // using String as equalTo with correct value. this will PASSED
    }


    //DELETE

    @Test
    public void deleteUserPositive(){

        int userIDdelete = 2;

        given().log().all()
                .when().delete("https://reqres.in/api/users/"+userIDdelete)
                .then()
                .log().all()
                .assertThat().statusCode(204);
    }

    @Test
    public void deleteUserNegative1(){

        int userIDdelete = 909090; //wrong userID

        given().log().all()
                .when().delete("https://reqres.in/api/users/"+userIDdelete)
                .then()
                .log().all()
                .assertThat().statusCode(304); //wrong status code
    }

    @Test
    public void deleteUserNegative2(){

        int userIDdelete = 2;

        given().log().all()
                .when().delete("https://reqres.in/api/users/"+userIDdelete)
                .then()
                .log().all()
                .assertThat().statusCode(304); //wrong status code
    }





}
