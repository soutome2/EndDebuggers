document.addEventListener('DOMContentLoaded', function() {

    // カルーセル内のテーブル生成
    function generateCarouselTable() {
        let carouselInner = document.getElementById('carousel-inner');
        if (!carouselInner) {
            console.error('Carousel inner container not found.');
            return;
        }
        carouselInner.innerHTML = ''; // 内容をクリア

        //for (let startIdx = 0; startIdx < dateList.length; startIdx += itemsPerPage) {
            let table = document.createElement('div');
            table.className = 'table-container'; // 新しく追加したクラス

            // テーブルの内容
            let tableContent = `
                
                <table class="table">
                    <thead id="table-head" class="table-head"></thead>
                    <tbody id="table-body" class="table-body"></tbody>
                </table>
            `;
            table.innerHTML = tableContent;


            carouselInner.appendChild(table);

            // ヘッダーの生成
            let tableHead = document.getElementById(`table-head`);
            let headRow = document.createElement('tr');
            headRow.innerHTML = '<th class="tableTime"></th>';
            for (let i = 0; i < headDateList.length; i++) {
                let head = headDateList[i];
                let th = document.createElement('th');
                th.className = 'tableHead1';
                th.textContent = head;
                headRow.appendChild(th);
            }
            tableHead.appendChild(headRow);

            // データの生成
            let tableBody = document.getElementById(`table-body`);
            for (let i = 0; i < timeList.length; i++) {
                let time = timeList[i];
                let hour = time.split(':')[0];
                let row = document.createElement('tr');
                row.innerHTML = `<td class="tableTime">${hour}時～</td>`;
                for (let j = 0; j < dateList.length; j++) {
                    let date = dateList[j];
                    let col = document.createElement('td');
                    col.className = 'tableData';
                    if (matrix[i][j] == 0) {
                        col.innerHTML = `<form action="ReserveInput" method="post">
                            <input type="hidden" name="date" value="${date}">
                            <input type="hidden" name="time" value="${time}">
                            <button type="submit" class="circle">〇</button>
                        </form>`;
                    } else {
                        col.innerHTML = `<span class="cross">×</span>`;
                    }
                    row.appendChild(col);
                }
                tableBody.appendChild(row);
            }
        }

        

    // DOMContentLoaded後にテーブル生成関数を呼び出す
    generateCarouselTable();
});