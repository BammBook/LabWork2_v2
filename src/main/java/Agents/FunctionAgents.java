package Agents;

import Behaviors.InitiatorBehavior;
import Behaviors.ReceiveCalculationRequest;
import Behaviors.ReceiveInitiatorRole;
import jade.core.Agent;

import java.util.concurrent.ThreadLocalRandom;

public class FunctionAgents extends Agent {
    /**
     * Поиск минимума от суммы трех функций. Каждую функцию (свою) вычисляет отдельный агент
     */

    String [] agentNames = {"A1", "A2", "A3"};


    @Override
    protected void setup() {
        System.out.println("Agent " + this.getLocalName() + " was started");

        if (this.getLocalName().endsWith("1"))
            this.addBehaviour(new InitiatorBehavior(this, agentNames, RandomBetween(0, 50), 10 + RandomBetween(0.01, 10)));
        this.addBehaviour(new ReceiveInitiatorRole(this, agentNames, 0, 0));
        this.addBehaviour(new ReceiveCalculationRequest(this));

    }
    public static double RandomBetween(double min, double max) {
        return (ThreadLocalRandom.current().nextDouble() * (max - min)) + min;
    }
}
