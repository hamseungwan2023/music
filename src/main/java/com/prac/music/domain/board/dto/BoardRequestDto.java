package com.prac.music.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

	private String title;

	private String contents;

	@Builder
	public BoardRequestDto(String title, String contents) {
		this.title = title;

		this.contents = contents;
	}
}
