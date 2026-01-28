## MongoDB Schema

### Collection: url_mapping

Fields:
- short_code (string, unique)
- long_url (string)
- created_at (timestamp)
- expiry_at (timestamp, optional)
- client_id (optional)

Indexes:
- Unique index on short_code

Notes:
- short_code is the primary lookup key
- Documents are immutable except expiry_at

## Local MongoDB Setup

MongoDB is run locally using Docker.

### Start MongoDB
```bash
docker compose up -d

