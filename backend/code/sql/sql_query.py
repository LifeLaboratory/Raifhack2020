SQL_SELECT_INFO_CLIENT = """
SELECT *
FROM raifhack.clients
WHERE id={id}
"""

SQL_SELECT_ORDERS_COURIER = """
SELECT *
FROM raifhack.orders
WHERE number_courier={id}
"""

SQL_SELECT_ALL_INFO = """
SELECT *
FROM raifhack.companies AS com
LEFT JOIN raifhack.couriers AS co ON (com.id = co.num_company)
LEFT JOIN raifhack.orders AS or ON (co.id = or.number_courier)
"""
