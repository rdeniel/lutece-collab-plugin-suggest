/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.digglike.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.digglike.utils.DiggUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for  objects CommentSubmitDAO
 */
public final class CommentSubmitDAO implements ICommentSubmitDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT MAX( id_comment_submit ) FROM digglike_comment_submit";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_comment_submit,id_digg_submit,date_comment,comment_value,active, lutece_user_key, official_answer, id_parent_comment,date_modify  " +
        "FROM digglike_comment_submit WHERE id_comment_submit=? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO digglike_comment_submit ( id_comment_submit,id_digg_submit,date_comment,comment_value,active,lutece_user_key,official_answer,id_parent_comment,date_modify ) " +
        "VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM digglike_comment_submit WHERE id_comment_submit = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE digglike_comment_submit SET " +
        "id_comment_submit=?,id_digg_submit=?,date_comment=?,comment_value=?,active=? ,lutece_user_key=? ,official_answer=? ,id_parent_comment=?" +
        " WHERE id_comment_submit=? ";
    private static final String SQL_QUERY_SELECT_COMMENT_SUBMIT_BY_FILTER = "SELECT id_comment_submit,id_digg_submit,date_comment,comment_value,active,lutece_user_key,official_answer,id_parent_comment,date_modify " +
        "FROM digglike_comment_submit ";
    private static final String SQL_QUERY_SELECT_COUNT_BY_FILTER = "SELECT COUNT(id_comment_submit) " +
        "FROM digglike_comment_submit INNER JOIN digglike_digg_submit ON digglike_digg_submit.id_digg_submit = digglike_comment_submit.id_digg_submit ";
    private static final String SQL_FILTER_ID_DIGG_SUBMIT = " digglike_comment_submit.id_digg_submit = ? ";
    private static final String SQL_FILTER_ID_PARENT_COMMENT = " id_parent_comment = ? ";
    private static final String SQL_FILTER_ID_DIGG = " id_digg = ? ";
    private static final String SQL_FILTER_COMMENT_SUBMIT_STATE = " active = ? ";
    private static final String SQL_FILTER_SORT_BY_DATE_COMMENT_DESC = " date_comment DESC";
    private static final String SQL_FILTER_SORT_BY_DATE_COMMENT_ASC = " date_comment ASC";
    private static final String SQL_FILTER_SORT_BY_DATE_MODIFY_COMMENT_DESC = " date_modify DESC";
    private static final String SQL_FILTER_SORT_BY_DATE_MODIFY_COMMENT_ASC = " date_modify ASC";
    private static final String SQL_QUERY_UPDATE_DATE_MODIFY = "UPDATE digglike_comment_submit SET date_modify=? WHERE id_comment_submit=? ";
        
    
   
    
    private static final String SQL_ORDER_BY = " ORDER BY ";


    /**
     * Generates a new primary key
     *
     * @param plugin the plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
         * Insert a new record in the table.
         *
         * @param commentSubmit instance of the Comment Submit object to insert
         * @param plugin the plugin
         *          */
    public void insert( CommentSubmit commentSubmit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        commentSubmit.setIdCommentSubmit( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, commentSubmit.getIdCommentSubmit(  ) );
        daoUtil.setInt( 2, commentSubmit.getDiggSubmit(  ).getIdDiggSubmit(  ) );
        daoUtil.setTimestamp( 3, commentSubmit.getDateComment(  ) );
        daoUtil.setString( 4, commentSubmit.getValue(  ) );
        daoUtil.setBoolean( 5, commentSubmit.isActive(  ) );
        daoUtil.setString( 6, commentSubmit.getLuteceUserKey(  ) );
        daoUtil.setBoolean( 7, commentSubmit.isOfficialAnswer(  ) );
        daoUtil.setInt( 8, commentSubmit.getIdParent(  ) );
        daoUtil.setTimestamp( 9, commentSubmit.getDateModify() );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
         * Load the data of the commentSubmit from the table
         *
         * @param nIdCommentSubmit The identifier of the comment submit
         * @param plugin the plugin
         * @return the instance of the commentSubmit
         */
    public CommentSubmit load( int nIdCommentSubmit, Plugin plugin )
    {
        CommentSubmit commentSubmit = null;
        DiggSubmit diggSubmit = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nIdCommentSubmit );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            commentSubmit = new CommentSubmit(  );
            commentSubmit.setIdCommentSubmit( daoUtil.getInt( 1 ) );

            diggSubmit = new DiggSubmit(  );
            diggSubmit.setIdDiggSubmit( daoUtil.getInt( 2 ) );
            commentSubmit.setDiggSubmit( diggSubmit );

            commentSubmit.setDateComment( daoUtil.getTimestamp( 3 ) );
            commentSubmit.setValue( daoUtil.getString( 4 ) );
            commentSubmit.setActive( daoUtil.getBoolean( 5 ) );
            commentSubmit.setLuteceUserKey( daoUtil.getString( 6 ) );
            commentSubmit.setOfficialAnswer( daoUtil.getBoolean( 7 ) );
            commentSubmit.setIdParent( daoUtil.getInt( 8 ) );
            commentSubmit.setDateModify( daoUtil.getTimestamp( 9 ) );
        }

        daoUtil.free(  );

        return commentSubmit;
    }

    /**
         * Delete   the comment submit whose identifier is specified in parameter
         *
         * @param nIdTagSubmit The identifier of the comment submit
         * @param plugin the plugin
         */
    public void delete( int nIdTagSubmit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdTagSubmit );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
         * Update the commentSubmit in the table
         *
         * @param commentSubmit instance of the commentSubmit object to update
         * @param plugin the plugin
         */
    public void store( CommentSubmit commentSubmit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, commentSubmit.getIdCommentSubmit(  ) );
        daoUtil.setInt( 2, commentSubmit.getDiggSubmit(  ).getIdDiggSubmit(  ) );
        daoUtil.setTimestamp( 3, commentSubmit.getDateComment(  ) );
        daoUtil.setString( 4, commentSubmit.getValue(  ) );
        daoUtil.setBoolean( 5, commentSubmit.isActive(  ) );
        daoUtil.setString( 6, commentSubmit.getLuteceUserKey(  ) );
        daoUtil.setBoolean( 7, commentSubmit.isOfficialAnswer(  ) );
        daoUtil.setInt( 8, commentSubmit.getIdParent(  ) );
        daoUtil.setInt( 9, commentSubmit.getIdCommentSubmit(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
 
    
    
public void storeDateModify( Timestamp dateModify,int idCommentSubmit, Plugin plugin )
{
    DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DATE_MODIFY, plugin );
    daoUtil.setTimestamp( 1, dateModify );
    daoUtil.setInt( 2, idCommentSubmit );
    daoUtil.executeUpdate(  );
    daoUtil.free(  );
}




    /**
     * Load the data of all the commentSubmit which verify the filter and returns them in a list
     * @param filter the filter
     * @param plugin the plugin
     * @return  the list of commentSubmit
     */
    public List<CommentSubmit> selectListByFilter( SubmitFilter filter, Plugin plugin )
    {
        List<CommentSubmit> commentSubmitList = new ArrayList<CommentSubmit>(  );
        CommentSubmit commentSubmit = null;
        DiggSubmit diggSubmit = null;
        List<String> listStrFilter = new ArrayList<String>(  );
        String strOrderBy = null;

        if ( filter.containsIdDiggSubmit(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_DIGG_SUBMIT );
        }

        if ( filter.containsIdCommentSubmitState(  ) )
        {
            listStrFilter.add( SQL_FILTER_COMMENT_SUBMIT_STATE );
        }
        if ( filter.containsIdParent() )
        {
            listStrFilter.add( SQL_FILTER_ID_PARENT_COMMENT );
        }
        

        if ( filter.containsSortBy(  ) )
        {
            strOrderBy = getOrderBy( filter.getSortBy(  ) );
        }

        String strSQL = DiggUtils.buildRequestWithFilter( SQL_QUERY_SELECT_COMMENT_SUBMIT_BY_FILTER, listStrFilter,
                strOrderBy );
        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( filter.containsIdDiggSubmit(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdDiggSubmit(  ) );
            nIndex++;
        }

        if ( filter.containsIdCommentSubmitState(  ) )
        {
            daoUtil.setBoolean( nIndex, filter.convertIdBoolean( filter.getIdCommentSubmitState(  ) ) );
            nIndex++;
        }
        if ( filter.containsIdParent(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdParent(  )  );
            nIndex++;
        }


        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            commentSubmit = new CommentSubmit(  );
            commentSubmit.setIdCommentSubmit( daoUtil.getInt( 1 ) );

            diggSubmit = new DiggSubmit(  );
            diggSubmit.setIdDiggSubmit( daoUtil.getInt( 2 ) );
            commentSubmit.setDiggSubmit( diggSubmit );

            commentSubmit.setDateComment( daoUtil.getTimestamp( 3 ) );
            commentSubmit.setValue( daoUtil.getString( 4 ) );
            commentSubmit.setActive( daoUtil.getBoolean( 5 ) );
            commentSubmit.setLuteceUserKey( daoUtil.getString( 6 ) );
            commentSubmit.setOfficialAnswer( daoUtil.getBoolean( 7 ) );
            commentSubmit.setIdParent( daoUtil.getInt( 8 ) );
            commentSubmit.setDateModify( daoUtil.getTimestamp( 9 ) );
            commentSubmitList.add( commentSubmit );
        }

        daoUtil.free(  );


        return commentSubmitList;
    }




    /**
     * return the number  of all the commentSubmit who verify the filter
     * @param filter the filter
     * @param plugin the plugin
     * @return  the number  of all the commentSubmit who verify the filter
     */
    public int selectCountByFilter( SubmitFilter filter, Plugin plugin )
    {
        int nIdCount = 0;
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( filter.containsIdDigg(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_DIGG );
        }

        if ( filter.containsIdDiggSubmit(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_DIGG_SUBMIT );
        }

        if ( filter.containsIdCommentSubmitState(  ) )
        {
            listStrFilter.add( SQL_FILTER_COMMENT_SUBMIT_STATE );
        }

        String strSQL = DiggUtils.buildRequestWithFilter( SQL_QUERY_SELECT_COUNT_BY_FILTER, listStrFilter, null );
        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( filter.containsIdDigg(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdDigg(  ) );
            nIndex++;
        }

        if ( filter.containsIdDiggSubmit(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdDiggSubmit(  ) );
            nIndex++;
        }

        if ( filter.containsIdCommentSubmitState(  ) )
        {
            daoUtil.setBoolean( nIndex, filter.convertIdBoolean( filter.getIdCommentSubmitState(  ) ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nIdCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nIdCount;
    }

    /**
     * build the order by clause
     * @param listSortBy list of sort by
     * @return the sql order by clause
     */
    private String getOrderBy( List<Integer> listSortBy )
    {
        StringBuffer strOrderBy = new StringBuffer(  );
        int ncpt = 0;

        if ( ( listSortBy != null ) && ( listSortBy.size(  ) != 0 ) )
        {
            strOrderBy.append( SQL_ORDER_BY );

            for ( Integer sort : listSortBy )
            {
                ncpt++;

                switch ( sort )
                {
                    case SubmitFilter.SORT_BY_DATE_RESPONSE_ASC:
                        strOrderBy.append( SQL_FILTER_SORT_BY_DATE_COMMENT_ASC );

                        break;

                    case SubmitFilter.SORT_BY_DATE_RESPONSE_DESC:
                        strOrderBy.append( SQL_FILTER_SORT_BY_DATE_COMMENT_DESC );

                        break;
                    case SubmitFilter.SORT_BY_DATE_MODIFY_ASC:
                        strOrderBy.append( SQL_FILTER_SORT_BY_DATE_MODIFY_COMMENT_ASC );

                        break;

                    case SubmitFilter.SORT_BY_DATE_MODIFY_DESC:
                        strOrderBy.append( SQL_FILTER_SORT_BY_DATE_MODIFY_COMMENT_DESC );

                        break;    

                    default:
                    	strOrderBy.append( SQL_FILTER_SORT_BY_DATE_MODIFY_COMMENT_DESC );
                    	
                    	break;
                }

                if ( ncpt < listSortBy.size(  ) )
                {
                    strOrderBy.append( "," );
                }
            }
        }

        return strOrderBy.toString(  );
    }
}
