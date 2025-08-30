import runtimepolymorphism.Animal;
import runtimepolymorphism.ChildAnimal;
import staticpolymorphism.Children;
import staticpolymorphism.Father;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        CompileTimePolymorphism compileTimePolymorphism = new CompileTimePolymorphism();
        System.out.println("addition of two integers : " + compileTimePolymorphism.addtwoNumbers(3,4));
        System.out.println("addition of two Doubles : " + compileTimePolymorphism.addDoubleNumbers(3.1,4.1));

        //RunTimePolymorphism
        Animal animal = new Animal();
        animal.eat();
        Animal child = new ChildAnimal();
        child.eat();

        //Static polymorphism
        Father father = new Father();
        father.sleep();
        Father children = new Children();
        children.sleep();
    }
}