def get_user_by_name(db_conn, name):
    query = "SELECT * FROM users WHERE name = '%s'" % name
    return db_conn.execute(query)
def get_user_by_name(db_conn, name):
    query = "SELECT * FROM users WHERE name = %s"
    return db_conn.execute(query, (name,))
