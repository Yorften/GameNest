package com.gamenest.events;

import org.springframework.context.ApplicationEvent;

import com.gamenest.model.Game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBuildEvent extends ApplicationEvent {
    private Game game;
    private String commit;

    public GameBuildEvent(Object source, Game game, String commit) {
        super(source);
        this.game = game;
        this.commit = commit;
    }

}
