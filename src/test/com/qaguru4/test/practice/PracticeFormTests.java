package com.qaguru4.test.practice;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PracticeFormTests {

    String URL = "https://demoqa.com/automation-practice-form",
            firstName = "John",
            lastName = "Snow",
            userEmail = "john@snow.wf",
            gender = "Other",
            mobileNumber = "9123456780",
            dayOfBirth = "29",
            monthOfBirth = "November",
            yearOfBirth = "1922",
            fileName = "cq5dam.web.1200.675.jpeg",
            currentAddress = "Winterfell Castle",
            state = "Haryana",
            city = "Panipat";

    String[] subjects = {"Accounting", "Computer Science", "Maths", "Physics"};
    String[] hobbies = {"Sports", "Reading", "Music"};

    String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getPath();

    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
    }

    @Test
    void practiceFormTest() {
        open(URL);
        $(".main-header").shouldHave(text("Practice Form"));

        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);
        $("#userEmail").setValue(userEmail);
        $(byText(gender)).parent().click();
        $("#userNumber").setValue(mobileNumber);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(monthOfBirth);
        $(".react-datepicker__year-select").selectOption(yearOfBirth);

/*
        This will fail in case when .react-datepicker__month contain two identical dayOfBirth
        numbers (for current and for previous/next month):
        $(".react-datepicker__month").$(byText(dayOfBirth)).click();
        No way to do it better :(
*/

        $(".react-datepicker__day--029:not(.react-datepicker__day--outside-month)").click();

        for (String hobbie : hobbies) {
            $(byText(hobbie)).parent().click();
        }

        SelenideElement subjectsInput = $("#subjectsInput");
        for (String subject : subjects) {
            subjectsInput.setValue(subject).pressTab();
        }

        $("#uploadPicture").uploadFile(new File(filePath));
        $("#currentAddress").setValue(currentAddress);
        $("#state input").setValue(state).pressTab();
        $("#city input").setValue(city).pressTab();

/*
        Will do the same as two last above but using cssSelector is preferable than using xpathExpression
        $x("//div[@id='state']//input").setValue(state).pressTab();
        $x("//div[@id='city']//input").setValue(city).pressTab();
*/

        $("#submit").click();

        $(".modal-header").shouldHave(text("Thanks for submitting the form"));
        $(".modal-body thead tr").shouldHave(text("Label")).shouldHave(text("Values"));
        $(".modal-body tbody tr", 0)
                .shouldHave(text("Student Name"))
                .shouldHave(text(firstName + " " + lastName));
        $(".modal-body tbody tr", 1)
                .shouldHave(text("Student Email"))
                .shouldHave(text(userEmail));
        $(".modal-body tbody tr", 2)
                .shouldHave(text("Gender"))
                .shouldHave(text(gender));
        $(".modal-body tbody tr", 3)
                .shouldHave(text("Mobile"))
                .shouldHave(text(mobileNumber));
        $(".modal-body tbody tr", 4)
                .shouldHave(text("Date of Birth"))
                .shouldHave(text(dayOfBirth + " " + monthOfBirth + "," + yearOfBirth));
        $(".modal-body tbody tr", 5)
                .shouldHave(text("Subjects"))
                .shouldHave(text(String.join(", ", subjects)));
        $(".modal-body tbody tr", 6)
                .shouldHave(text("Hobbies"))
                .shouldHave(text(String.join(", ", hobbies)));
        $(".modal-body tbody tr", 7).shouldHave(text("Picture"))
                .shouldHave(text(fileName));
        $(".modal-body tbody tr", 8)
                .shouldHave(text("Address"))
                .shouldHave(text(currentAddress));
        $(".modal-body tbody tr",9)
                .shouldHave(text("State and City"))
                .shouldHave(text(state + " " + city));
    }
}