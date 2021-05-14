class MemberRepository {
    fun join(
        loginId: String,
        loginPw: String,
        name: String,
        nickname: String,
        cellphoneNo: String,
        email: String
    ): Int {
        val id = getLastId() + 1
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()
        val member = Member(id, regDate, updateDate, loginId, loginPw, name, nickname, cellphoneNo, email)

        writeStrFile("data/member/${member.id}.json", member.toJson())
        setLastId(id)

        return id
    }

    fun getLastId(): Int {
        val lastId = readIntFromFile("data/member/lastId.txt", 0)

        return lastId
    }

    fun setLastId(newLastId: Int) {
        writeIntFile("data/member/lastId.txt", newLastId)
    }

    fun isJoinableLoginId(loginId: String): Boolean {
        val member = getMemberByLoginId(loginId)

        return member == null
    }

    fun getMemberByLoginId(loginId: String): Member? {
        val lastId = getLastId()

        for (id in 1..lastId) {
            val member = memberFromFile("data/member/$id.json")

            if ( member != null ) {
                if ( member.loginId == loginId ) {
                    return member
                }
            }
        }

        return null
    }

    fun makeTestMembers() {
        for (id in 1..9) {
            join("user${id}", "user${id}", "홍길동${id}", "사용자${id}", "0101234123${id}", "user${id}@test.com")
        }
    }

    fun getMemberById(id: Int): Member? {
        val member = memberFromFile("data/member/$id.json")

        return member
    }

    fun memberFromFile(jsonFilePath: String): Member? {
        val jsonStr = readStrFromFile(jsonFilePath)

        if (jsonStr == "") {
            return null
        }

        val map = mapFromJson(jsonStr)

        val id = map["id"].toString().toInt()
        val regDate = map["regDate"].toString()
        var updateDate = map["updateDate"].toString()
        var loginId = map["loginId"].toString()
        var loginPw = map["loginPw"].toString()
        var name = map["name"].toString()
        var nickname = map["nickname"].toString()
        var cellphoneNo = map["cellphoneNo"].toString()
        var email = map["email"].toString()

        return Member(id, regDate, updateDate, loginId, loginPw, name, nickname, cellphoneNo, email)
    }
}