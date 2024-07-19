
let startIdx = 0; // 初期の表示開始位置
let itemsPerPage = 7; // 1ページあたりのアイテム数

// ページネーションボタンの要素を取得
let prevButton = document.getElementById('prev-btn');
let nextButton = document.getElementById('next-btn');

// ページネーションボタンのクリックイベントリスナーを追加
prevButton.addEventListener('click', function() {
	if (startIdx - itemsPerPage >= 0) {
		startIdx -= itemsPerPage;
		renderTable();
	}
});

nextButton.addEventListener('click', function() {
	if (startIdx + itemsPerPage < dateList.length) {
		startIdx += itemsPerPage;
		renderTable();
	}
});

// 初期表示
renderTable();

// テーブルを描画する関数
function renderTable() {
	let tableHead = document.getElementById('table-head')
	let tableBody = document.getElementById('table-body');
	tableHead.innerHTML = '';
	tableBody.innerHTML = ''; // テーブルの内容をクリア

	let headRow = document.createElement('tr');
	headRow.innerHTML = `<th class="tableHead1"></th>`;
	for (let i = startIdx; i < startIdx + itemsPerPage && i < headDateList.length; i++) {
		let head = headDateList[i];
		let th = document.createElement('th');
		th.className = 'tableHead1';
		th.textContent = head; // テキストコンテンツを設定する場合はtextContentを使用する
		headRow.appendChild(th); // headRowに作成した<th>要素を追加する
	}
	tableHead.appendChild(headRow);

	for (let i = 0; i < timeList.length; i++) {
		let time = timeList[i];
		let hour = time.split(':')[0];
		let row = document.createElement('tr');
		row.innerHTML = `
		<td>${hour}時～</td> <!-- 時間を表示 -->
		`;
		for (let j = startIdx; j < startIdx + itemsPerPage && j < dateList.length; j++) {
			let date = dateList[j];
			let col = document.createElement('td');
			if (matrix[i][j] == 0) {
				col.innerHTML = `
				<form action="ReserveInput" method="post">
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