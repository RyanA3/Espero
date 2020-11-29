package me.felnstaren.espero.command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public abstract class CommandContinuator implements CommandElement, Tabbable {
	
	protected ArrayList<SubCommand> commands;
	protected ArrayList<SubArgument> arguments;
	protected CommandStub stub;
	protected String label;
	
	protected CommandContinuator(CommandStub stub, String label) {
		this.commands = new ArrayList<SubCommand>();
		this.arguments = new ArrayList<SubArgument>();
		this.stub = stub;
		this.label = label;
	}
	
	
	
	//Handle command sending
	public boolean handle(CommandSender sender, String[] args, int current) {
		return forward(sender, args, current);
	}
	
	protected boolean forward(CommandSender sender, String[] args, int current) {
		current++;
		
		if(current >= args.length) 
			return stub(sender, args);
		
		for(SubCommand sub : commands) 
			if(sub.getLabel().equals(args[current])) 
				if(sub.handle(sender, args, current)) return true;
		
		for(SubArgument arg : arguments) 
			if(arg.handle(sender, args, current)) return true;
		
		return false;
	}
	
	protected boolean stub(CommandSender sender, String[] args) {
		return stub.handle(sender, args, args.length - 1);
	}
	
	
	
	//Handle command tab-complete
	public ArrayList<String> tab(CommandSender sender, String[] args, int current) {
		return forwardTab(sender, args, current);
	}
	
	protected ArrayList<String> forwardTab(CommandSender sender, String[] args, int current) {
		current++;
		
		ArrayList<String> tabs = new ArrayList<String>();
		
		if(current >= args.length) {
			tabs.add(label);
			return tabs;
		}

		for(SubCommand sub : commands)
			tabs.addAll(sub.tab(sender, args, current));
		
		if(current > 0 && args[current-1].equals(label))
			for(SubArgument arg : arguments) 
				tabs.addAll(arg.tab(sender, args, current));
			
		return tabs;
	}
	
	
	
	//Getters & Setters
	public String getLabel() {
		return label;
	}
	
}
