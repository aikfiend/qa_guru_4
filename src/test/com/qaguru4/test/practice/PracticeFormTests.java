package com.qaguru4.test.practice;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.*;
import com.qaguru4.test.utils.*;
import org.junit.jupiter.api.*;

public class PracticeFormTests {

    static String URL = "https://demoqa.com/automation-practice-form",
            firstName = "John",
            lastName = "Snow",
            userEmail = "john@snow.wf",
            gender = "Other",
            mobileNumber = "9123456780",
            dayOfBirth = "1",
            monthOfBirth = "November",
            yearOfBirth = "1922",
            fileName = "JohnSnow.jpeg",
            currentAddress = "Winterfell Castle",
            state = "Haryana",
            city = "Panipat";

    String[] subjects = {"Accounting", "Computer Science", "Maths", "Physics"};
    String[] hobbies = {"Sports", "Reading", "Music"};

    static String dateOfBirth = String.format("%s %s, %s", monthOfBirth, dayOfBirth, yearOfBirth);

    @BeforeAll
    public static void chkDate() throws TestUtils.InvalidDateException {
        /*
            Date validation required for throwing exceptions in following cases:
            1) dayOfBirth = "0" (there would be flaky test in case of using with:
                $$(".react-datepicker__day:not(.react-datepicker__day--outside-month)").findBy(text(dayOfBirth)).click();
            2) dateOfBirth = "February 30, 1922"
            3) dateOfBirth = "1 Jan 1922"
        */
        TestUtils.isValidDate(dateOfBirth);
    }

    @Test
    void practiceFormFillTest() {
        open(URL);
        $(".main-header").shouldHave(text("Practice Form"));

        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);
        $("#userEmail").setValue(userEmail);
        $(byText(gender)).click();
        $("#userNumber").setValue(mobileNumber);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(monthOfBirth);
        $(".react-datepicker__year-select").selectOption(yearOfBirth);
        $(".react-datepicker__day[aria-label*='" + TestUtils.ordinalDayDateFormat(dateOfBirth) + "']").click();

        /*
            Will do the same, but without TestUtils.ordinalDayDateFormat():
            $$(".react-datepicker__day:not(.react-datepicker__day--outside-month)").findBy(text(dayOfBirth)).click();
        */

        SelenideElement subjectsInput = $("#subjectsInput");
        for (String subject : subjects) {
            subjectsInput.setValue(subject).pressTab();
        }

        for (String hobbie : hobbies) {
            $(byText(hobbie)).click();
        }

        $("#uploadPicture").uploadFromClasspath(fileName);
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
                .shouldHave(text(String.format("%s %s", firstName, lastName)));
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
                .shouldHave(text(String.format("%s %s,%s", dayOfBirth, monthOfBirth, yearOfBirth)));
        $(".modal-body tbody tr", 5)
                .shouldHave(text("Subjects"))
                .shouldHave(text(String.join(", ", subjects)));
        $(".modal-body tbody tr", 6)
                .shouldHave(text("Hobbies"))
                .shouldHave(text(String.join(", ", hobbies)));
        $(".modal-body tbody tr", 7)
                .shouldHave(text("Picture"))
                .shouldHave(text(fileName));
        $(".modal-body tbody tr", 8)
                .shouldHave(text("Address"))
                .shouldHave(text(currentAddress));
        $(".modal-body tbody tr",9)
                .shouldHave(text("State and City"))
                .shouldHave(text(String.format("%s %s", state, city)));
    }
}