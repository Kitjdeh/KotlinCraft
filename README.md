# KotlinCraft

## 앱 소개

개발 기간: 2023.10~

주요 기능: 계산기 기능 및 산술 결과를 확인 하는 기능

Github 링크:https://github.com/Kitjdeh/KotlinCraft

KotlinCraft : Kotlin을 기본기부터 차근차근 복습하기 위한 Craft(공예품을 만들다)작업

## Structure

https://file.notion.so/f/f/d5e30a5b-7cae-462a-8f51-4993236d0f1a/b009ed3e-8ab2-4b81-82de-d5e0ba29d02d/Untitled.png?id=33546b4d-e8f8-4be7-8bd4-7afc5008fded&table=block&spaceId=d5e30a5b-7cae-462a-8f51-4993236d0f1a&expirationTimestamp=1702303200000&signature=dgNo_FwCRtnyASVyPvPswiGJ9UQutSFC31w6uekZoc4&downloadName=Untitled.png


## 사용기술

- Target SDK Level 33
- Min SDK Level 33
- kotlin : 1.8.20

## 목표

1. **계산기 패드 UI 구현 O**
2. **계산기 로직 구현 O**
    - 후위 표현식 변경 함수, 연산 함수 완료
3. **Room을 통한 로컬 DB 구축 O**
4. **기록 확인 페이지 생성 및 결과를 리스트로 보여주기 O**
    - RecyclewView의 Adapter에 DB데이터 연동
5. **결과 값 크기에 따른 색상 구현 O**
6. **롱클릭으로 삭제 O**
    - setOnLongClickListenr에 interface listener를 넣어 구현
7. **드래그 앤 드롭으로 순서 변경 O**
    - **I**temTouchHelper.Callback()를 통해 구현

## 화면

https://file.notion.so/f/f/d5e30a5b-7cae-462a-8f51-4993236d0f1a/f505d9ec-6e5b-4af7-be41-1426eae70e10/write.gif?id=2da5cc26-be41-4086-9384-a6d26d3b304e&table=block&spaceId=d5e30a5b-7cae-462a-8f51-4993236d0f1a&expirationTimestamp=1702303200000&signature=_fb7vHx0ZW7FVxZdZjGSPWDZ3Z5TwT1WKmg1OUK0ses&downloadName=write.gif

![image](https://github.com/Kitjdeh/KotlinCraft/assets/109275661/1334daa7-bc61-4cb5-b3cc-cef0547baccb)


## 핵심 로직

### 1. 계산기 연산 로직

우리가 수식을 계산하는 과정은 전체 수식을 본 후 사칙연산과 괄호()를 토대로 순서를 정한다.(중위 연산자) 하지만 컴퓨터는 이러한 순서를 한번에 알아보지 못하기 때문에 모든 순서를 고려해서 배열을 바꿔줘야 한다.(후위 연산자)

이러한 점을 토대로 계산기에 입력한 중위 연산자를 후위 연산자로 바꾸는 postFix(), 변경 된 후위연산자를 계산하는 calculateStack()으로 나누어 진행한다.

![image](https://github.com/Kitjdeh/KotlinCraft/assets/109275661/e7d35be4-1689-4736-9a40-11da994e73a8)


- **후위 연산자로 변경 (postFix())**

핵심 로직은 다음과 같다.

1. 숫자가 들어오면 words에 넣는다.
    - 단, “3” “.” “4” → 3.4로 받아야 하니까 연산자 나오기 전까지 word에 추가하다가 연산자가 나오면 words에 추가
    - 3 4 . 5 + →  34.5 → words에 추가
2. 연산자가 들어오면 words을 후위표기 postFixStack에 넣고 stack에서 자기 보다 우선순위가 높거나 같은 것들은 빼고 나는 stack에 들어간다.
    
    stack - [ + * ] 에서 - 입력 → stack [ - ] 만 남기고 * + 를 추가
    
3. (는 무조건 stack 에 담고 )를 만나면 (나올 때 가지 stack에서 출력
4. expression 순회가 끝나면 남은 stack을 postFIxStack에 넣는다.
- 세부 코드
    
    ```kotlin
    private fun postFix() {
            // 2자리수 이상의 string 일경우 words에 포함시켜서 진행
            var words = ""
            for (char in expression) {
                val word = char.toString()
                if (word.isDigitsOnly() || word == ".") {
                    //.이 이미 있다면 break
                    if (words.contains(".") && word == ".") {
                        expression = getString(R.string.value_error)
    //                    Log.d("postFi 244",expression)
                        Toast.makeText(this, expression, Toast.LENGTH_SHORT).show()
                        break
                    }
                    words += word
    
                } else {
                    //isDigt가 아니다 -> 연산 or ()이니 계산된 words 를 넣어준다.
                    if (words.isNotEmpty()) {
                        postFixStack.add(words)
                        words = ""
                    }
                    if (word == "(") {
                        stack.add(word.toString())
                    }
    
                    // ")" 가 나올 경우 해당 )과 맞아 떨어지는 (이 나오기 전까지 stack들을 postFixStack에 집어넣는다.(앞자리로 보내서 먼저 계산 할 수 있도록 만든다.)
                    else if (word == ")") {
                        while (stack.isNotEmpty() && stack.last() != "(") {
                            postFixStack.add(stack.pop())
                        }
                        // while 이 종료되었다면 stack의 맨뒤가 ( 이거나 stack이 비어있음 -> pop()로 정리
                        stack.pop()
                    } else if (word == "*" || word == "/") {
                        // *과 / 가 나올경우 이전에 나온 *나 /가 먼저기 때문에 해당 값들을 후위 표현식에 넣어준다.
                        while (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                            postFixStack.add(stack.pop())
                        }
                        stack.add(word.toString())
    
                    } else if (word == "+" || word == "-") {
                        // +와 - 는 우선순이가 제일 낮기 때문에 () 기준으로 가장 뒤에 가도록 후위 표현식에 배치한다.
                        while (stack.isNotEmpty() && stack.last() != "(") {
                            postFixStack.add(stack.pop())
                        }
                        stack.add(word.toString())
                    }
                }
            }
            if (words.isNotEmpty()) {
                postFixStack.add(words)
            }
            while (stack.isNotEmpty()) {
                postFixStack.add(stack.pop())
            }
            calculateStack()
        }
    ```
    
- **후위 연산자 계산**

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/d5e30a5b-7cae-462a-8f51-4993236d0f1a/03304de2-0f6c-49cc-a277-d76ae8eac0cc/Untitled.png)

1. 숫자는 스택(resultStack)에 담는다.
2. 연산자를 만나면 스택에서 연산자 2개를 꺼내서 연산하고 다시 넣는다.
    
    이때 stack의 길이가 2보다 작다면 —> 수식이 잘못된 것이니 break
    
3. 연산을 다 하고 스택에 남아있는 숫자를 결과값 expression으로
- 세부 코드
    
    ```kotlin
    private fun calculateStack() {
            loop@ for (num in postFixStack) {
                if (num.isDigitsOnly() || num.contains(".")) {
                    resultStack.add(num.toDouble())
                } else {
                    if (num.isDigitsOnly() || resultStack.size < 2) {
                        expression = getString(R.string.value_error)
    //                    Log.d("calculateStack 301",expression)
                        Toast.makeText(this, expression, Toast.LENGTH_SHORT).show()
                        break@loop
                    }
                    val secondNum = resultStack.pop()
                    val firstNum = resultStack.pop()
                    if (num == "+") {
                        val answer = firstNum + secondNum
                        resultStack.add(answer)
                    } else if (num == "-") {
                        val answer = firstNum - secondNum
                        resultStack.add(answer)
                    } else if (num == "*") {
                        val answer = firstNum * secondNum
                        resultStack.add(answer)
                    } else if (num == "/") {
                        if (secondNum.equals(0.0)) {
                            expression = getString(R.string.value_zero)
                            Toast.makeText(this, expression, Toast.LENGTH_SHORT).show()
                            break@loop
                        } else {
                            if ((round(secondNum * 1000) / 1000).roundToInt() == 0) {
                                expression = ""
                                break@loop
                            }
                            val answer = firstNum / secondNum
                            resultStack.add(answer)
                        }
                    }
                }
            }
            if (expression != getString(R.string.value_error) && expression != getString(R.string.value_zero)) {
    //            //결과값 저장
    //            val recordDao = db.recordDao()
                expression = resultStack.pop().toString()
                // DB에 접근 할 대 메인 쓰레드를 쓰면 에러가 나기 때문에 Dispathcer.io로 백그라운드 스레드에서 작업
                CoroutineScope(Dispatchers.IO).launch {
                    saveExpression += "=$expression"
                    val record = Record(null, saveExpression)
                    db!!.recordDao().insertRecord(record)
                }
            }
        }
    ```
    

### 2. 리스너 설정

리스트 뷰의 item을 다루는 Adatper는 메인스레드의 UI작업이다. 이곳에서 백그라운드 스레드 작업인 Room Database작업을 진행 하지 않기 위해 interface를 통한 콜백 리스너 작업을 진행하였다.

Adpater에서 리스트 뷰에 롱클릭 시 Record를 삭제하는 함수

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/d5e30a5b-7cae-462a-8f51-4993236d0f1a/07667739-8c54-4c8f-a9da-8f061fe15ff5/Untitled.png)

- 메인 스레드(UI작업)에 필요한 백그라운드 스레드 작업을 RecordActivity에서 선언

```kotlin
class RecordActivity : AppCompatActivity(
) {
    private lateinit var rv_record: RecyclerView

    private val listener = object : LongClickListener {
        // 구체적인 함수의 기능을 담은 deleteRecord를 알려준다.
				override fun delete(record: Record) {
            deleteRecord(record)
        }
        override fun change(start: Int, end: Int) {
            changeRecord(start, end)
        }
    }
)
		private fun initRecyclerView() {
        rv_record.apply {
            layoutManager = LinearLayoutManager(this@RecordActivity)
            adapter = recordAdapter
        }
        recordAdapter.addListener(listener)
}

```

```kotlin
class RecordAdapter(
) :

// 우선 선언만 해놓는다.
private lateinit var listener: LongClickListener

//위의 RecordActivty에서 만든 listener로 갈아 끼운다.
fun addListener(listener: LongClickListener) {
        this.listener = listener
    }
```

- Adapter에서 콜백 리스너 작동

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/d5e30a5b-7cae-462a-8f51-4993236d0f1a/38b35c48-f33c-4c47-9fba-bd1c24bbc552/Untitled.png)

```kotlin
class RecordAdapter(
) :

// 이벤트 발생시 받아온 delete에 데이터를 넣고 실행시킨다.
holder.itemView.setOnLongClickListener {
            listener.delete(Items[position])
            return@setOnLongClickListener true
        }
```

받아온 데이터를 백그라운드 스레드에서 실행 시킨다.

```kotlin
@SuppressLint("NotifyDataSetChanged")
    private fun deleteRecord(record: Record) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("삭제하시겠습니까?")
            .setPositiveButton(
                "삭제",
                DialogInterface.OnClickListener { dialog, which ->
                    //백그라운드 스레드에서 데이터 삭제
                    CoroutineScope(Dispatchers.IO).launch {
                        val recordDao = db!!.recordDao()
                        recordDao.deleteRecord(record)
                        records = recordDao.getAll() as ArrayList<Record>
                        withContext(Dispatchers.Main) {
                            recordAdapter.notifyDataSetChanged()
                        }
                    }
                }
            )
            .setNegativeButton("취소", null)
        builder.show()
    }
```
