package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("select  * from NOTES where userid = #{userId}")
    List<Note> getAllNotes(Integer userId);

    @Select("select * from NOTES where noteid = #{noteId} and userid = #{userId}")
    Note getNoteById(Integer noteId, Integer userId);

    @Insert("insert into NOTES(notetitle, notedescription, userid) values (#{noteTitle}, #{noteDescription}, #{userId})")
    int addNote(Note note);

    @Delete("delete from NOTES where noteid = #{noteId} and userid = #{userId}")
    int deleteNoteById(Integer noteId, Integer userId);

    @Update({"update NOTES set notetitle = #{noteTitle}, notedescription = #{noteDescription} where noteid = #{noteId}"})
    int updateNoteById(Note note);
}
