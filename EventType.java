package events;

/**
 * The available event types.
 *
 */
public enum EventType {
	None,
	KeyPressed,
	KeyReleased,
	TimeEvent,
	KeyboardEvent,
	MouseEvent,
	ActionAvailableEvent,
	DoActionEvent,
	LifeEvent,
	CoinsEvent,
	UpdateEvent,
	ShutdownRequestEvent,
	GraphicsEngineClosedEvent,
	LogEvent,
	LogConsolePrintTriggerEvent,
	NextLevelEvent,
	DeathEvent,
	HelpScreenEvent,
	LevelWin,
	LevelLoss,
	PlayerMoved
}
