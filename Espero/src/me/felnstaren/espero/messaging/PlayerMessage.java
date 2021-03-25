package me.felnstaren.espero.messaging;

public enum PlayerMessage {
	
	ERROR_COLOR("#F55"),
	ERROR_TRANSACTION_CANCELLED(ERROR_COLOR + "Transaction Cancelled"),
	ERROR_NOT_IN_NATION(ERROR_COLOR + "You must be in a nation to do this!"),
	ERROR_NATION_PERMISSION(ERROR_COLOR + "You do not have permission to do this in your nation!"),
	ERROR_CHUNK_ALREADY_CLAIMED(ERROR_COLOR + "This chunk has already been claimed!"),
	ERROR_IN_NATION(ERROR_COLOR + "You must leave your nation first to do this!"),
	ERROR_NAME_TAKEN(ERROR_COLOR + "This name has already been taken!"),
	ERROR_TOO_LONG(ERROR_COLOR + "This name may not be more than %length% characters long!"),
	ERROR_PLAYER_NOT_ONLINE(ERROR_COLOR + "This player is not online right now!"),
	ERROR_PLAYER_IN_SEPERATE_NATION(ERROR_COLOR + "This player is not in your nation!"),
	ERROR_CANT_DEMOTE(ERROR_COLOR + "You cannot demote this player!"),
	ERROR_CANT_KICK(ERROR_COLOR + "You cannot kick this player!"), 
	ERROR_INVALID_ARGUMENT(ERROR_COLOR + "Invalid Argument: %argument%"),
	ERROR_NOT_INVITED(ERROR_COLOR + "You have not been invited by this nation!"),
	ERROR_COFFERS_OUT_NATION(ERROR_COLOR + "You can only access your coffers in your own nation!");
	
	
	private String message;
	
	private PlayerMessage(String message) {
		this.message = message;
	}
	
	public String message() {
		return message;
	}
	
	@Override
	public String toString() {
		return message;
	}

}
