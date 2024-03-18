let currentExpandedTable = null; // Tracks the currently expanded table

function createTableRow(headers, rowData) {
  const row = document.createElement('tr');

  headers.forEach(header => {
    const cell = row.insertCell();

    switch (header) {
      case "URL":
        const link = document.createElement('a');
        link.href = rowData.url;
        link.textContent = rowData.url;
        link.target = '_blank';
        cell.appendChild(link);
        break;
      case "QR Code":
        const qrCodeElement = document.createElement('div');
        qrCodeElement.classList.add('qr-code-cell');
        new QRCode(qrCodeElement, {
          text: rowData.url,
          width: 64,
          height: 64,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.H
        });
        cell.appendChild(qrCodeElement);
        break;
      case "Action":
        const emailButton = document.createElement('button');
        emailButton.classList.add('action-cell');
        emailButton.textContent = 'Email URL';
        emailButton.onclick = function() {
          const emailBody = `Here is the URL: ${rowData.url}`;
          window.open(`mailto:?subject=${rowData.env} URL&body=${encodeURIComponent(emailBody)}`);
        };
        cell.appendChild(emailButton);
        break;
      default:
        cell.textContent = rowData[header.toLowerCase()] || '';
    }

    cell.style.border = '1px solid black';
  });

  return row;
}

function createCollapsibleTable(tableData, isFirstTable) {
  const urlsContainer = document.getElementById('urls');
  const title = tableData.title;
  const headers = tableData.headers;
  const rows = tableData.urls;

  const collapsibleButton = document.createElement('button');
  collapsibleButton.innerHTML = isFirstTable ? '- ' + title : '+ ' + title;
  collapsibleButton.classList.add('collapsible');

  const tableContainer = document.createElement('div');
  tableContainer.style.display = isFirstTable ? 'block' : 'none';
  tableContainer.classList.add('content');
  urlsContainer.appendChild(collapsibleButton);
  urlsContainer.appendChild(tableContainer);

  if (isFirstTable) {
    currentExpandedTable = tableContainer;
  }

  collapsibleButton.addEventListener('click', function() {
    const isExpanded = tableContainer.style.display === 'block';
    if (!isExpanded) {
      if (currentExpandedTable) {
        currentExpandedTable.style.display = 'none';
        currentExpandedTable.previousElementSibling.innerHTML = '+ ' + currentExpandedTable.previousElementSibling.textContent.slice(2);
      }
      tableContainer.style.display = 'block';
      this.innerHTML = '- ' + title;
      currentExpandedTable = tableContainer;
    } else {
      tableContainer.style.display = 'none';
      this.innerHTML = '+ ' + title;
      currentExpandedTable = null;
    }
  });

  const table = document.createElement('table');
  table.style.borderCollapse = 'collapse';
  tableContainer.appendChild(table);

  const headerRow = table.createTHead().insertRow();
  headers.forEach(headerText => {
    const headerCell = document.createElement('th');
    headerCell.textContent = headerText;
    headerCell.style.border = '1px solid black';
    headerCell.style.fontWeight = 'bold';
    headerRow.appendChild(headerCell);
  });

  rows.forEach(rowData => {
    table.appendChild(createTableRow(headers, rowData)); // Pass the individual rowData here
  });
}

function loadData() {
  fetch(chrome.runtime.getURL('data.json'))
    .then(response => response.json())
    .then(data => {
      let isFirstTable = true;
      for (const tableName in data) {
        if (data.hasOwnProperty(tableName)) {
          createCollapsibleTable(data[tableName], isFirstTable);
          isFirstTable = false;
        }
      }
    })
    .catch(error => console.error('Error loading JSON data:', error));
}

document.addEventListener('DOMContentLoaded', function() {
  loadData();
});
