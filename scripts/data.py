import random
import bcrypt
import uuid
from datetime import date, timedelta
from faker import Faker
import psycopg2
from psycopg2.extras import execute_values

fake = Faker("en_GB")


COURSE_TEMPLATES = [
    ("Computer Science", "CS"),
    ("Data Science", "DS"),
    ("Cyber Security", "CY"),
    ("Accounting And Bussiness Management", "AB"),
    ("Financial Technology", "FT"),
    ("Animation", "AA"),
    ("Games Art", "GA"),
    ("Graphic Design", "GD"),
    ("Computer Games Programming", "GP"),
    ("Marketing", "MA"),
    ("Marketing (Digital Innovation)", "MI"),
    ("Physcial Education", "PE"),
    ("Sport Business Management", "SM"),
    ("Sports Journalism", "SJ"),
]

MODULE_TEMPLATES = [
    ("Distributed Databases and Data Warehousing", "CT6049"),
    ("Malware Analysis", "CT6044"),
    ("Advanced Networking", "CT6034"),
    ("Dissertation", "CT6039"),
    ("Secure Coding", "CT6042"),
    ("Cyber Law", "BM7214"),
    ("Business Development Project", "BM7111"),
    ("Big Data in Finance", "FT7102"),
    ("Artifical Intelligence", "FT7206"),
    ("Block Chain And Applications", "FT7103"),
    ("Introduction to Law", "AC4104"),
    ("Fundamentals Of Financial Accouting", "AC4101"),
    ("Introduction to Economics", "AC4105"),
    ("Advanced Tax", "AC6107"),
    ("Political Economy", "AC6111"),
    ("Major Animation Project", "MT6001"),
    ("The Case Study", "MT6003"),
    ("Animation Workplace", "MT6006"),
    ("Advanced Group Project", "CT6008"),
    ("3D Character Development", "CT6035"),
    ("Creative Awards", "AD6002"),
    ("Self Promotion", "AD6403"),
    ("Graphics Programming With Shaders", "Ct6025"),
    ("Advanced AI Algorithms", "CT6024"),
    ("Indie Game Development", "CT6018"),
    ("Strategic Marketing", "MS6101"),
    ("Capstone Bussiness Project", "BM6725"),
    ("Ai and Marketing", "MS6209"),
    ("Marketing Agency", "BM6175"),
    ("Loyalty and Communications", "MS6109"),
    ("Third Sector Marketing", "MS6210"),
    ("Teaching Practice", "SP6251"),
    ("Health Promotion for wellness and Wellbeing", "SP6368"),
    ("Media Careers", "MD6126"),
    ("Professional Sports Desk", "MD6111"),
    ("Ethics, Censorship, Aand Regulation", "MD6101"),
]

STAFF = [
    ("admin", "password", "ADMIN"),
    ("staff1", "change", "DECISION_MAKER"),
    ("staff2", "change", "DECISION_MAKER"),
    ("staff3", "change", "DECISION_MAKER"),
    ("staff4", "change", "DECISION_MAKER"),
    ("staff5", "change", "DECISION_MAKER"),
]

BOOKS = [
    ("9780261103573", "The Lord of the Rings", "J,R,R Tolkien", 10),
    ("9780134685991", "Effective Java", "Joshua Bloch", 3),
    ("9781501110368", "It Ends With Us", "Colleen Hoover", 0),
    ("9781087939278", "Twisted Love", "Ana Huang", 2),
    ("9780451159274", "It", "Stephen King", 11),
    ("9780007548231", "A Game of Thrones", "George R. R. Martin", 3),
    ("9781407135397", "The Hunger Games", "Suzanne Collins", 7),
    ("9780132350884", "Clean Code", "Robert C. Martin", 3),
    ("9780201633610", "Design Patterns", "Erich Gamma et al.", 2),
    ("9780262046305", "Introduction to Algorithms", "Thomas H. Cormen et al.", 4),
    ("9780078022159", "Database System Concepts", "Abraham Silberschatz et al.", 3),
    ("9780262035613", "Deep Learning", "Ian Goodfellow et al.", 2),
    ("9781492041139", "Data Science from Scratch", "Joel Grus", 2),
    ("9780134444284", "Cryptography and Network Security", "William Stallings", 3),
    ("9781449373320", "Designing Data-Intensive Applications", "Martin Kleppmann", 4),
    ("9781118530801", "The Data Warehouse Toolkit", "Ralph Kimball, Margy Ross", 2),
    ("9781942788294", "The Phoenix Project", "Gene Kim et al.", 3),
    ("9780321982384", "Linear Algebra and Its Applications", "David C. Lay et al.", 3),
    ("9781593279288", "Python Crash Course", "Eric Matthes", 4),
    ("9780132126953", "Computer Networks", "Andrew S. Tanenbaum, David Wetherall", 3),
    ("9780134527338", "Network Security Essentials", "William Stallings", 3),
    ("9781491957660", "Python for Data Analysis", "Wes McKinney", 3),
    ("9780201896848", "The Art of Computer Programming, Vol. 1", "Donald E. Knuth", 2),
    ("9780135957059", "The Pragmatic Programmer", "David Thomas, Andrew Hunt", 3),
    ("9780201485677", "Refactoring", "Martin Fowler", 2),
    ("9780321125217", "Domain-Driven Design", "Eric Evans", 2),
    ("9780596007126", "Head First Design Patterns", "Eric Freeman et al.", 3),
    ("9781617294136", "Deep Learning with Python", "François Chollet", 3),
    ("9781593271442", "Hacking: The Art of Exploitation", "Jon Erickson", 2),
    ("9781934356555", "SQL Antipatterns", "Bill Karwin", 2),
    ("9781491963418", "PostgreSQL: Up and Running", "Regina Obe, Leo Hsu", 2),
    ("9781617293726", "Kubernetes in Action", "Marko Luksa", 2),
    ("9781449355739", "Learning Python", "Mark Lutz", 3),
    ("9780134101613", "Computer Organization and Architecture", "William Stallings", 3),
    ("9780393929720", "Statistics", "David Freedman et al.", 2),
    ("9781492087830", "Data Pipelines Pocket Reference", "James Densmore", 2),
    ("9781593273880", "The Tangled Web", "Michal Zalewski", 2),
    ("9781492080510", "High Performance MySQL", "Silvia Botros, Jeremy Tinley", 2),
    ("9780201896831", "The Art of Computer Programming, Vol. 2", "Donald E. Knuth", 2),
    ("0394506561", "Andy Warhol", "Andy Warhol", 2),
    ("9781891024177", "Raymond Pettibon", "Raymond Pettibon", 11),
    ("9780321965516", "Dont Make Me Think, Revisted", "Steve Krug", 2),
    ("9788535241983", "Arte de Game Design", "Jesse Schell", 1),
    ("0879511885", "Graphic design", "Milton Glaser", 5),
    ("0131101633", "The C Programming Language", "Brain W. Kernighan", 2),
    ("0385191952", "Hackers", "Steven Levy", 9),
    ("9781593275990", "Automate the Boring Stuff with Python", "Al Sweigart", 2),
    (
        "9781491918661",
        "Learning PHP, MySQL & JavaScript: With JQuery, CSS & HTML5",
        "Robin Nixon",
        20,
    ),
    ("0130204358", "Strucuted computer organization", "Andrew S. Tanenbaum", 2),
    ("9780370332284", "Wonder", "R.J. Palacio", 2),
    ("0440543053", "Catch-22", "Joseph Heller", 8),
    ("9781804090114", "The Psychology of Money", "Morgan Housel", 6),
    (
        "9780671663988",
        "the 7 Habits of Highly Effective People",
        "Stephen R. Covey",
        11,
    ),
    ("9780385669740", "The power of habit", "Charles Duhigg", 23),
    ("0394745027", "Godel, Escher, Bach", "Douglas R. Hofstadter", 18),
    ("9780399165245", "A Mind for Numbers", "Barbara A. Oakley", 13),
    ("0812975219", "Fooled by randomness", "Nassim Nicholas Taleb", 9),
    ("9781491901427", "Data science from scratch", "Joel Grus", 2),
    ("9780073309293", "Calculus", "Robert Thomas Smith", 5),
    (
        "0873533054",
        "Discrete mathemeatics across the cirriculum, K-12",
        "Margaret J, Kenney",
        13,
    ),
    ("0395861624", "Snowflake Bentley", "Jacqueline Briggs Martin", 12),
    ("0899193803", "Lincoln", "Russell Freedman", 21),
    ("9780140390773", "The Marble Faun", "Nathaniel Hawthorne", 19),
    ("9780393341355", "The coral sea", "Patti Smith", 23),
]


def hash_password(password):
    return bcrypt.hashpw(password.encode(), bcrypt.gensalt()).decode()


def loan_status_and_dates(loan_date: date):
    due_date = loan_date + timedelta(days=14)
    today = date.today()

    if due_date >= today:
        return due_date, None, "BORROWED", 0.0

    days_late = max(0, (today - due_date).days)
    days_from_loan = (today - loan_date).days

    if days_from_loan <= 21 and random.random() < 0.4:
        fine = round(min(days_late, 14) * 1.50, 2)
        return due_date, None, "OVERDUE", fine

    fake_late = random.randint(0, min(days_late, 14))
    return_date = due_date + timedelta(days=fake_late)
    fine = round(days_late * 1.50, 2)
    return due_date, return_date, "RETURNED", fine


def main():
    conn = psycopg2.connect(
        host="localhost", dbname="libdb", user="libuser", password="libpass", port=5432
    )
    cursor = conn.cursor()

    cursor.execute("""
        TRUNCATE loans, students, staff, users,
                 course_modules, modules, courses
        CASCADE
    """)

    #####################
    # Courses & Modules #
    #####################
    course_ids = []
    course_data = []
    for name, code in COURSE_TEMPLATES:
        cid = str(uuid.uuid4())
        course_ids.append(cid)
        course_data.append((cid, name, f"{code}-{random.randint(100, 499)}"))

    execute_values(
        cursor,
        "INSERT INTO courses (course_id, course_name, course_code) VALUES %s",
        course_data,
    )

    module_ids = []
    module_data = []
    for name, code in MODULE_TEMPLATES:
        mid = str(uuid.uuid4())
        module_ids.append(mid)
        module_data.append((mid, name, code))

    execute_values(
        cursor,
        "INSERT INTO modules (module_id, module_name, module_code) VALUES %s",
        module_data,
    )

    course_modules_data = set()
    for cid in course_ids:
        for mid in random.sample(module_ids, k=random.randint(3, len(module_ids))):
            course_modules_data.add((cid, mid))

    execute_values(
        cursor,
        "INSERT INTO course_modules (course_id, module_id) VALUES %s",
        list(course_modules_data),
    )

    #########
    # Staff #
    #########
    staff_user_data = []
    staff_data = []
    for username, password, role in STAFF:
        uid = str(uuid.uuid4())
        created_at = fake.date_time_between(start_date="-3y", end_date="-1y")
        staff_user_data.append(
            (uid, username, hash_password(password), created_at, None)
        )
        staff_data.append((str(uuid.uuid4()), uid, role))

    execute_values(
        cursor,
        "INSERT INTO users (id, username, password_hash, created_at, last_login) VALUES %s",
        staff_user_data,
    )
    execute_values(
        cursor,
        "INSERT INTO staff (staff_id, user_id, role) VALUES %s",
        staff_data,
    )

    ############
    # Students #
    ############
    student_user_data = []
    student_data = []
    student_meta = []
    student_pass_hash = hash_password("change")

    for i in range(3000):
        uid = str(uuid.uuid4())
        username = fake.unique.user_name()[:50]

        enrolled = fake.date_between(start_date="-5y", end_date="-14d")
        years_since = (date.today() - enrolled).days / 365

        if years_since >= 3:
            study_year = 3
            study_end = enrolled + timedelta(days=365 * 3)
        else:
            study_year = min(int(years_since) + 1, 3)
            study_end = date.today()

        # created_at = fake.date_time_between(start_date="-5y", end_date="-14d")
        last_login = (
            fake.date_time_between(start_date=enrolled, end_date=study_end)
            if random.random() < 0.95
            else None
        )
        student_user_data.append(
            (
                uid,
                username,
                student_pass_hash,
                enrolled,
                last_login,
            )
        )
        student_number = f"S{i:06}"
        course_id = random.choice(course_ids)
        student_data.append(
            (str(uuid.uuid4()), uid, student_number, course_id, study_year)
        )

        student_meta.append((uid, study_year, enrolled, study_end))

    execute_values(
        cursor,
        "INSERT INTO users (id, username, password_hash, created_at, last_login) VALUES %s",
        student_user_data,
    )
    execute_values(
        cursor,
        "INSERT INTO students (student_id, user_id, student_number, course_id, study_year) VALUES %s",
        student_data,
    )

    #########
    # Books #
    #########
    book_ids = []
    book_data = []

    for isbn, title, author, copies in BOOKS:
        bid = str(uuid.uuid4())
        book_ids.append(bid)
        book_data.append((bid, isbn, title, author, copies))

    execute_values(
        cursor,
        "INSERT INTO books (id, isbn, title, author, copies_left) VALUES %s",
        book_data,
    )

    #########
    # Loans #
    #########
    today = date.today()

    loan_weight = []
    for uid, study_year, enrolled, study_end in student_meta:
        weight = {1: 1, 2: 1, 3: 2}.get(study_year, 1)
        loan_weight.extend([(uid, enrolled, study_end)] * weight)

    loan_data = []

    while len(loan_data) < 15000:
        uid, enrolled, study_end = random.choice(loan_weight)
        latest = min(study_end - timedelta(days=1), today - timedelta(days=1))
        if enrolled >= latest:
            continue

        loan_date = fake.date_between(start_date=enrolled, end_date=latest)

        due_date, return_date, status, fine_amount = loan_status_and_dates(loan_date)

        loan_data.append(
            (
                str(uuid.uuid4()),
                uid,
                random.choice(book_ids),
                loan_date,
                due_date,
                return_date,
                status,
                fine_amount,
            )
        )

    execute_values(
        cursor,
        """INSERT INTO loans
           (id, user_id, book_id, loan_date, due_date, return_date, status, fine_amount)
           VALUES %s""",
        loan_data,
    )

    conn.commit()


if __name__ == "__main__":
    main()
