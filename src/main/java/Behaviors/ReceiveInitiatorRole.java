package Behaviors;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveInitiatorRole extends Behaviour {
    private Agent myAgent;
    private MessageTemplate mt;
    private String[] neighbourAgent;
    private double curX;
    private double curDelta;

    public ReceiveInitiatorRole(Agent myAgent, String[] neighbourAgent, double curX, double curDelta) {
        this.myAgent = myAgent;
        this.neighbourAgent = neighbourAgent;
        this.curX = curX;
        this.curDelta = curDelta;
    }

    @Override
    public void action() {
        mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if(msg != null){
            String[] str = msg.getContent().split(" - ");
            curX = Double.parseDouble(str[1]);
            curDelta = Double.parseDouble(str[2]);
            System.out.println("I'm " + myAgent.getLocalName() + ". I have received Initiator role from " + msg.getSender().getLocalName());
            myAgent.addBehaviour(new InitiatorBehavior(myAgent, neighbourAgent, curX, curDelta));
        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
