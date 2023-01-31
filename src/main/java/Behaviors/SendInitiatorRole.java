package Behaviors;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendInitiatorRole extends OneShotBehaviour {
    private String nextAgent;
    private Double X;
    private Double delta;

    public SendInitiatorRole(String nextAgent, Double x, Double delta) {
        this.nextAgent = nextAgent;
        X = x;
        this.delta = delta;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID n = new AID(nextAgent, false);
        msg.addReceiver(n);
        msg.setContent("Next initiator is: "+nextAgent + ". Data for calculation - " + X + " - " + delta );
        myAgent.send(msg);
        myAgent.removeBehaviour(this);
    }


}
