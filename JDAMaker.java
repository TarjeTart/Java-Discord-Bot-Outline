public class JDAMaker {
	
	String token, setGame;
	
	public JDAMaker(String token,String setGame) {
		this.token = token;
		this.setGame = setGame;
	}
	
	public void constructJDA(){
		try
        {
            JDA jda = JDABuilder.createLight(token)// The token of the account that is logging in.
            		.addEventListeners(new Driver())// An instance of a class that will handle events.
            		
                    .build();
            jda.getPresence().setActivity(Activity.playing(setGame));
            jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
	}

}
