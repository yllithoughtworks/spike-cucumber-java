import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.junit.Assertions;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {
    Cukes cukes;
    @Given("^I have (\\d+) cukes$")
    public void I_have_cukes_in_my_belly(int count) {
        cukes=new Cukes();
        cukes.setCukes(count);

    }


    @Given("^I eat (\\d+) cukes")
    public void eat_cuke(int eatead){
        cukes.eat(eatead);
    }

    @Then("^There are (\\d+) cukes left$")
    public void left_cook(int left){
        assertEquals(cukes.getCukes(),left);
    }

}