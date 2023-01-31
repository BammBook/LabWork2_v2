package Behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static Agents.OptimizationFunctions.*;


public class ReceiveCalculationRequest extends Behaviour {

    private Agent myAgent;
    private MessageTemplate mt;
    private Double X;
    private Double delta;


    public ReceiveCalculationRequest(Agent myAgent) {
        this.myAgent = myAgent;
    }

    @Override
    public void onStart() {
        mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if(msg != null){
            String[] str = msg.getContent().split(" - ");
            X = Double.parseDouble(str[0]);
            delta = Double.parseDouble(str[1]);

//           Сообщение тому, кто сделал запрос
            ACLMessage answMsg = new ACLMessage(ACLMessage.PROPOSE);
            answMsg.setConversationId("resultOfCalculation");
            AID n = new AID(msg.getSender().getLocalName(), false);
            answMsg.addReceiver(n);
            switch (myAgent.getLocalName()) {
                case "A1" ->
                        answMsg.setContent(FirstFunction(X - delta) + " - " + FirstFunction(X) + " - " + FirstFunction(X + delta));
                case "A2" ->
                        answMsg.setContent(SecondFunction(X - delta) + " - " + SecondFunction(X) + " - " + SecondFunction(X + delta));
                case "A3" ->
                        answMsg.setContent(ThirdFunction(X - delta) + " - " + ThirdFunction(X) + " - " + ThirdFunction(X + delta));
            }
            myAgent.send(answMsg);
//            System.out.println(myAgent.getLocalName() + ": " + answMsg.getContent());

        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
