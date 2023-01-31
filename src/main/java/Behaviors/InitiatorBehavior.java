package Behaviors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InitiatorBehavior extends Behaviour {

    private String[] neighbourAgent;
    private Agent myAgent;
    private double X;
    private double delta;
    private double minY;
    private final double eps = 0.01;
    private double curYleft = 0, curY = 0, curYright = 0;
    private MessageTemplate mt;
    private MessageTemplate mtAnsw;
    private int receiversCounter = 0;
    private boolean changeInitiator = true;
    private boolean stop = false;
    private boolean finishCalculation = false;

    public InitiatorBehavior(Agent myAgent, String[] neighbourAgent, double x, double delta) {
        super(myAgent);
        this.myAgent = myAgent;
        this.neighbourAgent = neighbourAgent;
        this.X = x;
        this.delta = delta;

    }

    @Override
    public void onStart() {
        System.out.println();
        System.out.println(myAgent.getLocalName() + " is Initiator");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        for (int i = 0; i < neighbourAgent.length; i++) {
            AID n = new AID(neighbourAgent[i], false);
            msg.addReceiver(n);
        }
        msg.setContent(X + " - " + delta);
        myAgent.send(msg);

        mtAnsw = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchConversationId("resultOfCalculation"));
    }

    @Override
    public void action() {
        ACLMessage answMsg = myAgent.receive(mtAnsw);
        if (answMsg != null) {
            String[] str = answMsg.getContent().split(" - ");
            curYleft += Double.parseDouble(str[0]);
            curY += Double.parseDouble(str[1]);
            curYright += Double.parseDouble(str[2]);
            receiversCounter++;
            if (receiversCounter == neighbourAgent.length) { // -> получены все ответы
                stop = true;
            }
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        minY = min(curYleft, curY, curYright);
        System.out.println("Current X: " + X + "; Current delta: " + delta);
        System.out.println("Current Y: " + minY);

        if (minY == curYleft)
            X = X - delta;
        if (minY == curYright)
            X = X + delta;

        if (minY == curY){
            if (delta < eps){
                System.out.println();
                System.out.println("Result of calculation: " + curY);
                finishCalculation = true;
            } else {
                delta /= 2;
                System.out.println(myAgent.getLocalName() + " stays Initiator");
                changeInitiator = false;
            }
        }

//        myAgent.removeBehaviour(this);

        if (!finishCalculation){
            if (changeInitiator){
                String nextAgent = neighbourAgent[getRandomNumber(0, neighbourAgent.length-1)];
                myAgent.addBehaviour(new SendInitiatorRole(nextAgent, X, delta));
            } else {
                myAgent.addBehaviour(new InitiatorBehavior(myAgent, neighbourAgent, X, delta));
            }
        }
        return 1;
    }

    @Override
    public boolean done() {
        return stop;
    }

    public int getRandomNumber(int min, int max) {
        return (int) Math.round((Math.random() * (max - min)) + min);
    }

    public static double min(double a, double b, double c) {
        return Math.min(a, Math.min(b, c));
    }

}