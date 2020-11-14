from sql.sql_query import *
from base.base_sql import Sql

id = 1
query = SQL_SELECT_INFO_CLIENT.format(id=id)
answer = Sql.exec(query=query)
print(answer)