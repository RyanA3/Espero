package me.felnstaren.espero.command;

public abstract class SubCommand extends CommandContinuator {

	public SubCommand(CommandStub stub, String label) {
		super(stub, label);
	}

}
