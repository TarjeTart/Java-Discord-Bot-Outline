package Music.Testing.Bot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class JDAMaker {
	
	String token, setGame;
	
	public JDAMaker(String token,String setGame) {
		this.token = token;
		this.setGame = setGame;
	}
	
	public void constructJda(){
		try {
            JDA jda = JDABuilder.createLight(token)
            			.addEventListeners(new Driver())// An instance of a class that will handle events.
            			.build();
			jda.getPresence().setActivity(Activity.playing(setGame));
           		jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            		System.out.println("Finished Building JDA!");
        	}catch (LoginException e) {
           		 e.printStackTrace();
        	}catch (InterruptedException e) {
            		e.printStackTrace();
       		}
	}
	
	public void constructJdaWithMusic(){
		try {
           		JDA jda = JDABuilder.createDefault(token,
            			GatewayIntent.GUILD_MEMBERS,
            			GatewayIntent.GUILD_MESSAGES,
            			GatewayIntent.GUILD_VOICE_STATES/*this is only needed for music bots*/)
            			.addEventListeners(new Driver())// An instance of a class that will handle events.
            			.enableCache(CacheFlag.VOICE_STATE)//only need this for music bots
                   	 	.build();
            		jda.getPresence().setActivity(Activity.playing(setGame));
           		jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            		System.out.println("Finished Building JDA!");
        	} catch (LoginException e) {
           		 e.printStackTrace();
        	} catch (InterruptedException e) {
            		e.printStackTrace();
       		}
	}

}
