from code import api
from flask_restplus import Resource, Api
from base.base_sql import Sql
from sql.sql_query import *


class Provider:
    @classmethod
    def get_client_info(cls, client_id):
        query = SQL_SELECT_INFO_CLIENT.format(id=client_id)
        return Sql.exec(query=query)

