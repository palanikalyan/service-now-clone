-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, role) VALUES
    ('admin', 'admin@example.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ADMIN');

-- Insert some test users (password: user123)
INSERT INTO users (username, email, password, role) VALUES
                                                        ('john_doe', 'john@example.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'USER'),
                                                        ('jane_smith', 'jane@example.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'USER');

-- Insert some test devices
INSERT INTO devices (hostname, ip_address, assigned_user_id) VALUES
                                                                 ('laptop-001', '192.168.1.100', 2),
                                                                 ('laptop-002', '192.168.1.101', 2),
                                                                 ('desktop-001', '192.168.1.102', 3);

-- Insert some test software requests
INSERT INTO software_requests (requested_by_id, device_id, software_name, status, created_at, updated_at) VALUES
                                                                                                              (2, 1, 'Visual Studio Code', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                              (2, 1, 'Docker Desktop', 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                              (3, 3, 'IntelliJ IDEA', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);