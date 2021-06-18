public class Driver extends ListenerAdapter{

public static void main(String[] args){
        //these two lines create a jda instance and launch it
        JDAMaker jda = new JDAMaker("your token here","-help");
        //use constructJdaWithMusic for music bots
        jda.constructJda();
    }
    //this an example of a listener for an event, see jda documentation for all events
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //this is a class that holds instructions for when bot recieves a message
        //change to MessageRecievedMusic for music bot example
        new MessageRecieved(event);
    }//ends message received event    
}
