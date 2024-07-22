document.addEventListener('DOMContentLoaded', function() {
	let itemsPerPage = 7; // 1ページあたりのアイテム数
	let currentPageIndex = 0; // 現在のページインデックスを管理する変数

	// カルーセル内のテーブル生成
	function generateCarouselTable() {
		let carouselInner = document.getElementById('carousel-inner');
		if (!carouselInner) {
			console.error('Carousel inner container not found.');
			return;
		}
		carouselInner.innerHTML = ''; // 内容をクリア

		for (let startIdx = 0; startIdx < dateList.length; startIdx += itemsPerPage) {
			let table = document.createElement('div');
			table.className = 'table-container'; // 新しく追加したクラス

			// テーブルの内容
			let tableContent = `
                <h5>${startIdx / itemsPerPage + 1}週目</h5>
                <table class="table">
                    <thead id="table-head-${startIdx}" class="table-head"></thead>
                    <tbody id="table-body-${startIdx}" class="table-body"></tbody>
                </table>
            `;
			table.innerHTML = tableContent;

			// テーブルを非表示にする
			if (startIdx !== 0) {
				table.style.display = 'none';
			}

			carouselInner.appendChild(table);

			// ヘッダーの生成
			let tableHead = document.getElementById(`table-head-${startIdx}`);
			if (!tableHead) {
				console.error(`Table head element with id "table-head-${startIdx}" not found.`);
				continue; // 次のループに進む
			}
			let headRow = document.createElement('tr');
			headRow.innerHTML = '<th class="tableTime"></th>';
			for (let i = startIdx; i < startIdx + itemsPerPage && i < headDateList.length; i++) {
				let head = headDateList[i];
				let th = document.createElement('th');
				th.className = 'tableHead1';
				th.textContent = head;
				headRow.appendChild(th);
			}
			tableHead.appendChild(headRow);

			// データの生成
			let tableBody = document.getElementById(`table-body-${startIdx}`);
			if (!tableBody) {
				console.error(`Table body element with id "table-body-${startIdx}" not found.`);
				continue; // 次のループに進む
			}
			for (let i = 0; i < timeList.length; i++) {
				let time = timeList[i];
				let hour = time.split(':')[0];
				let row = document.createElement('tr');
				row.innerHTML = `<td class="tableTime">${hour}時～</td>`;
				for (let j = startIdx; j < startIdx + itemsPerPage && j < dateList.length; j++) {
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

		// ボタンで次のテーブルを表示する関数
		function showNextTable() {
			// 現在のテーブルを非表示にする
			let currentTable = document.querySelector('.table-container:nth-child(' + (currentPageIndex + 1) + ')');
			if (currentTable) {
				currentTable.style.display = 'none';
			}

			// 次のページインデックスを計算
			currentPageIndex++;
			if (currentPageIndex >= Math.ceil(dateList.length / itemsPerPage)) {
				currentPageIndex = 0; // 最初に戻る
			}

			// 次のテーブルを表示する
			let nextTable = document.querySelector('.table-container:nth-child(' + (currentPageIndex + 1) + ')');
			if (nextTable) {
				nextTable.style.display = 'block';
			}
		}

		// prevボタンで前のテーブルを表示する関数
		function showPrevTable() {
			// 現在のテーブルを非表示にする
			let currentTable = document.querySelector('.table-container:nth-child(' + (currentPageIndex + 1) + ')');
			if (currentTable) {
				currentTable.style.display = 'none';
			}

			// 前のページインデックスを計算
			currentPageIndex--;
			if (currentPageIndex < 0) {
				currentPageIndex = Math.ceil(dateList.length / itemsPerPage) - 1; // 最後に戻る
			}

			// 前のテーブルを表示する
			let prevTable = document.querySelector('.table-container:nth-child(' + (currentPageIndex + 1) + ')');
			if (prevTable) {
				prevTable.style.display = 'block';
			}
		}

		// ボタンにイベントリスナーを追加
		let nextButton = document.getElementById('nextButton');
		if (nextButton) {
			nextButton.addEventListener('click', showNextTable);
		}

		// ボタンにイベントリスナーを追加
		let prevButton = document.getElementById('prevButton');
		if (prevButton) {
			prevButton.addEventListener('click', showPrevTable);
		}

		// 最初のページをアクティブにする
		let firstTable = document.querySelector('.table-container:first-child');
		if (firstTable) {
			firstTable.style.display = 'block';
		}
	}

	// DOMContentLoaded後にテーブル生成関数を呼び出す
	generateCarouselTable();
});