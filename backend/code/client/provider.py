from code.base.base_sql import Sql
from code.sql.sql_query import *


class Provider:
    @classmethod
    def get_client_info(cls, client_id):
        query = SQL_SELECT_INFO_CLIENT.format(id=client_id)
        return Sql.exec(query=query)

    @classmethod
    def get_clients_info(cls):
        query = SQL_SELECT_INFO_CLIENTS
        return Sql.exec(query=query)

