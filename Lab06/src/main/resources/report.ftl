<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Movies Report</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=DM+Serif+Display&family=DM+Sans:wght@300;400;500&display=swap');

        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'DM Sans', sans-serif;
            background: #0d0d0d;
            color: #e8e0d0;
            min-height: 100vh;
            padding: 60px 40px;
        }

        header {
            text-align: center;
            margin-bottom: 60px;
        }

        header h1 {
            font-family: 'DM Serif Display', serif;
            font-size: 3.5rem;
            letter-spacing: -1px;
            color: #f5e6c8;
        }

        header p {
            margin-top: 10px;
            font-size: 0.9rem;
            color: #777;
            letter-spacing: 2px;
            text-transform: uppercase;
        }

        .table-wrap {
            max-width: 1100px;
            margin: 0 auto;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead th {
            font-size: 0.7rem;
            letter-spacing: 2px;
            text-transform: uppercase;
            color: #888;
            padding: 12px 20px;
            border-bottom: 1px solid #2a2a2a;
            text-align: left;
        }

        tbody tr {
            border-bottom: 1px solid #1a1a1a;
            transition: background 0.15s;
        }

        tbody tr:hover { background: #161616; }

        tbody td {
            padding: 18px 20px;
            font-size: 0.95rem;
            vertical-align: middle;
        }

        .title-cell {
            font-family: 'DM Serif Display', serif;
            font-size: 1.1rem;
            color: #f5e6c8;
        }

        .score {
            display: inline-block;
            background: #1e1e1e;
            border: 1px solid #2e2e2e;
            border-radius: 20px;
            padding: 3px 12px;
            font-size: 0.85rem;
            font-weight: 500;
            color: #c8a96e;
        }

        .genre-tag {
            display: inline-block;
            background: #1a1a2e;
            border-radius: 4px;
            padding: 3px 10px;
            font-size: 0.8rem;
            color: #8888cc;
        }

        .actors-cell {
            color: #999;
            font-size: 0.88rem;
        }

        footer {
            text-align: center;
            margin-top: 60px;
            font-size: 0.8rem;
            color: #444;
        }
    </style>
</head>
<body>

<header>
    <h1>Movies Report</h1>
    <p>Generated on ${generatedAt}</p>
</header>

<div class="table-wrap">
    <table>
        <thead>
            <tr>
                <th>Title</th>
                <th>Genre</th>
                <th>Release Date</th>
                <th>Duration</th>
                <th>Score</th>
                <th>Actors</th>
            </tr>
        </thead>
        <tbody>
        <#list movies as movie>
            <tr>
                <td class="title-cell">${movie.title}</td>
                <td><span class="genre-tag">${movie.genre}</span></td>
                <td>${movie.releaseDate}</td>
                <td>${movie.duration} min</td>
                <td><span class="score">★ ${movie.score}</span></td>
                <td class="actors-cell">${movie.actors}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<footer>
    <p>${movies?size} movies total</p>
</footer>

</body>
</html>
