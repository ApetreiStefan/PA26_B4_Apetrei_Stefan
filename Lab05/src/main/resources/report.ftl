<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Repository Catalog Report</title>
    <style>
        body { font-family: sans-serif; margin: 40px; background-color: #f4f7f6; }
        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.2); }
        th { background-color: #3498db; color: white; text-align: left; padding: 12px; }
        td { padding: 12px; border-bottom: 1px solid #eee; }
        tr:hover { background-color: #f1f1f1; }
        .location { font-family: monospace; font-size: 0.9em; color: #666; }
    </style>
</head>
<body>

<h1>Catalog Resources</h1>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Author</th>
        <th>Year</th>
        <th>Location</th>
    </tr>
    </thead>
    <tbody>
    <#list resources as item>
        <tr>
            <td><strong>${item.id!""}</strong></td>
            <td>${item.title!"Unknown Title"}</td>
            <td>${item.author!"N/A"}</td>
            <td>${item.year!""}</td>
            <td class="location">${item.location!""}</td>
        </tr>
    </#list>
    </tbody>
</table>

<p style="margin-top: 20px; font-size: 0.8em; color: #888;">
    Report generated on: ${.now?string["yyyy-MM-dd HH:mm:ss"]}
</p>

</body>
</html>