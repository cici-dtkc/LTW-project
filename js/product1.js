
// data.js
export const products = [
    {
        id: 1,
        brand_id: 1,
        category_id: 1,
        name: "iPhone 15 Pro Max 256GB",
        price: 29990000,
        img: "assert/img/product/iphone15.jpg",
        discount_percentage: 5,
        total_sold: 3200,
        description:
            "iPhone 15 Pro Max được trang bị chip A17 Pro mạnh mẽ, khung viền titanium sang trọng, camera tele 5x, màn hình Super Retina XDR 120Hz cho trải nghiệm vượt trội.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2023-09-22T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Titan tự nhiên", "Titan xanh", "Titan trắng", "Titan đen"],
        versions: ["256GB", "512GB", "1TB"],
        slides: [
            "assert/img/product/iphone15_behind.jpg",
            "assert/img/product/iphone15_after.jpg",
            "assert/img/product/iphone15_camera.jpg"
        ],
        specs: {
            screen: "Super Retina XDR OLED 6.7 inch",
            chip: "Apple A17 Pro 6 nhân",
            ram: "8 GB",
            storage: "256 GB",
            camera: "Chính 48 MP & Phụ 12 MP, 12 MP",
            battery: "4422 mAh, sạc nhanh 20W",
            os: "iOS 17",
            sim: "2 SIM (nano-SIM và eSIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 2,
        brand_id: 1,
        category_id: 1,
        name: "iPhone 14 128GB",
        price: 17990000,
        img: "assert/img/product/iphone14.jpg",
        discount_percentage: 10,
        total_sold: 4100,
        description:
            "iPhone 14 vẫn giữ thiết kế tinh tế, hiệu năng mạnh mẽ từ chip A15 Bionic, camera kép chất lượng cao và thời lượng pin ổn định.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2022-09-16T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Tím", "Đỏ", "Trắng", "Xanh dương", "Vàng"],
        versions: ["128GB", "256GB", "512GB"],
        slides: [
            "assert/img/product/iphone14_behind.jpg",
            "assert/img/product/iphone14_after.jpg",
            "assert/img/product/iphone14_camera.jpg"
        ],
        specs: {
            screen: "OLED 6.1 inch, Super Retina XDR",
            chip: "Apple A15 Bionic 6 nhân",
            ram: "6 GB",
            storage: "128 GB",
            camera: "Chính 12 MP & Phụ 12 MP, 12 MP",
            battery: "3279 mAh, sạc nhanh 20W",
            os: "iOS 16",
            sim: "2 SIM (nano-SIM và eSIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 3,
        brand_id: 5,
        category_id: 1,
        name: "Vivo V30 5G 256GB",
        price: 12990000,
        img: "assert/img/product/vivoV30e.jpg",
        discount_percentage: 8,
        total_sold: 1800,
        description:
            "Vivo V30 nổi bật với camera chân dung Aura Light, màn hình AMOLED cong 120Hz, chip Snapdragon 7 Gen 3 cho hiệu năng ổn định.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2024-03-01T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Xanh Hồng", "Đen", "Trắng"],
        versions: ["8GB/128GB", "12GB/256GB"],
        slides: [
            "assert/img/product/vivoV30e_behind.jpg",
            "assert/img/product/vivoV30e_after.jpg",
            "assert/img/product/vivoV30e_camera.jpg"
        ],
        specs: {
            screen: "AMOLED 6.78 inch, 120Hz",
            chip: "Snapdragon 7 Gen 1",
            ram: "12 GB",
            storage: "256 GB",
            camera: "Chính 50 MP & Phụ 50 MP, 50 MP",
            battery: "5000 mAh, sạc nhanh 80W",
            os: "Android 14, FuntouchOS 14",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 4,
        brand_id: 5,
        category_id: 1,
        name: "Vivo Y19 6G 128GB",
        price: 7990000,
        img: "assert/img/product/vivoV30e.jpg",
        discount_percentage: 7,
        total_sold: 950,
        description:
            "Vivo Y19 mang thiết kế trẻ trung, camera chính 50MP, hiệu năng ổn định với chip Dimensity 6020 và sạc nhanh 80W.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2024-05-10T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Xanh ngọc", "Tím", "Đen"],
        versions: ["8GB/128GB", "8GB/256GB"],
        slides: [
            "assert/img/product/vivoY19_behind.jpg",
            "assert/img/product/vivoY19_after.jpg",
            "assert/img/product/vivoY19_camera.jpg"
        ],
        specs: {
            screen: "AMOLED 6.67 inch, 120Hz",
            chip: "Snapdragon 685",
            ram: "8 GB",
            storage: "128 GB",
            camera: "Chính 50 MP & Phụ 2 MP, 8 MP",
            battery: "5000 mAh, sạc nhanh 44W",
            os: "Android 14",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 5,
        brand_id: 6,
        category_id: 1,
        name: "Realme 14T 5G 12GB/256GB",
        price: 14990000,
        img: "assert/img/product/realme14T.jpg",
        discount_percentage: 6,
        total_sold: 1500,
        description:
            "Realme 14T sở hữu thiết kế mô phỏng đồng hồ sang trọng, camera tiềm vọng 64MP và hiệu năng mạnh với Snapdragon 7s Gen 2.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2024-02-15T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Xanh đại dương", "Be cát"],
        versions: ["8GB/256GB", "12GB/512GB"],
        slides: [
            "assert/img/product/realme14T_behind.jpg",
            "assert/img/product/realme14T_after.jpg",
            "assert/img/product/realme14T_camera.jpg"
        ],
        specs: {
            screen: "AMOLED 6.7 inch, 120Hz",
            chip: "Snapdragon 7s Gen 2",
            ram: "8 GB",
            storage: "256 GB",
            camera: "Chính 50 MP & Phụ 64 MP, 8 MP, 32 MP",
            battery: "5000 mAh, sạc nhanh 67W",
            os: "Android 14, Realme UI 5.0",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 6,
        brand_id: 6,
        category_id: 1,
        name: "Realme C67 8GB/128GB",
        price: 6490000,
        img: "assert/img/product/realmeC71.jpg",
        discount_percentage: 10,
        total_sold: 2100,
        description:
            "Realme C67 sở hữu màn hình 90Hz, camera 108MP, chip Snapdragon 685 và pin 5000mAh cùng sạc nhanh 33W.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2023-12-15T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Xanh lá", "Đen ánh kim"],
        versions: ["6GB/128GB", "8GB/128GB"],
        slides: [
            "assert/img/product/realmeC71_behind.jpg",
            "assert/img/product/realmeC71_after.jpg",
            "assert/img/product/realmeC71_camera.jpg"
        ],
        specs: {
            screen: "IPS LCD 6.72 inch, 90Hz",
            chip: "Snapdragon 685",
            ram: "8 GB",
            storage: "128 GB",
            camera: "Chính 108 MP & Phụ 2 MP, 8 MP",
            battery: "5000 mAh, sạc nhanh 33W",
            os: "Android 14",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 7,
        brand_id: 9,
        category_id: 1,
        name: "Sony Xperia 1 V 12GB/256GB",
        price: 27990000,
        img: "assert/img/product/sony_xperia.jpg",
        discount_percentage: 4,
        total_sold: 800,
        description:
            "Sony Xperia 1 V mang đến trải nghiệm quay phim chuyên nghiệp, camera Zeiss, màn hình 4K OLED 120Hz và chip Snapdragon 8 Gen 2.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2023-07-01T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Đen", "Bạc", "Xanh olive"],
        versions: ["12GB/256GB"],
        slides: [
            "assert/img/product/sony_xperia_behind.jpg",
            "assert/img/product/sony_xperia_camera.jpg"
        ],
        specs: {
            screen: "OLED 6.5 inch, 4K HDR, 120Hz",
            chip: "Snapdragon 8 Gen 2",
            ram: "12 GB",
            storage: "256 GB",
            camera: "Chính 48 MP & Phụ 12 MP, 12 MP",
            battery: "5000 mAh, sạc nhanh 30W",
            os: "Android 13",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Thái Lan"
        }
    },
    {
        id: 8,
        brand_id: 9,
        category_id: 1,
        name: "Sony Xperia 5 V 8GB/128GB",
        price: 21990000,
        img: "assert/img/product/sony_xperiaX.jpg",
        discount_percentage: 5,
        total_sold: 700,
        description:
            "Sony Xperia 5 V có thiết kế nhỏ gọn, camera cảm biến Exmor T, hiệu năng mạnh với Snapdragon 8 Gen 2 và màn hình OLED HDR.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2023-10-15T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Tím", "Trắng", "Đen"],
        versions: ["8GB/128GB"],
        slides: [
            "assert/img/product/sony_xperiaX_behind.jpg",
            "assert/img/product/sony_xperiaX_after.jpg",
            "assert/img/product/sony_xperiaX_camera.jpg"
        ],
        specs: {
            screen: "OLED 6.1 inch, Full HD+",
            chip: "Snapdragon 695 5G",
            ram: "8 GB",
            storage: "128 GB",
            camera: "Chính 48 MP & Phụ 8 MP, 8 MP",
            battery: "5000 mAh, sạc nhanh 21W",
            os: "Android 13",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Nhật Bản"
        }
    },
    {
        id: 9,
        brand_id: 10,
        category_id: 1,
        name: "Honor 400 Pro 5G 256GB",
        price: 8990000,
        img: "assert/img/product/honor400.jpg",
        discount_percentage: 7,
        total_sold: 650,
        description:
            "Honor 200 Lite sở hữu camera selfie 50MP, màn hình AMOLED 90Hz, pin 4500mAh và thiết kế trẻ trung sang trọng.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2024-04-15T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Xanh dương", "Đen", "Trắng ngọc"],
        versions: ["12GB/512GB", "12GB/256GB"],
        slides: [
            "assert/img/product/honor400_behind.jpg",
            "assert/img/product/honor400_after.jpg",
            "assert/img/product/honor400_camera.jpg"
        ],
        specs: {
            screen: "OLED 6.7 inch, 120Hz",
            chip: "Snapdragon 7 Gen 1",
            ram: "12 GB",
            storage: "256 GB",
            camera: "Chính 50 MP & Phụ 50 MP, 12 MP, 50 MP",
            battery: "5200 mAh, sạc nhanh 100W",
            os: "Android 14, MagicOS 8.0",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Trung Quốc"
        }
    },
    {
        id: 10,
        brand_id: 10,
        category_id: 1,
        name: "Honor X9b 5G 256GB",
        price: 8990000,
        img: "assert/img/product/honorx9c.jpg",
        discount_percentage: 6,
        total_sold: 900,
        description:
            "Honor X9b với pin 5800mAh, thiết kế cong ấn tượng, camera 108MP và độ bền đạt chuẩn cao cấp.",
        status: 1,
        created_at: "2025-10-31T09:00:00",
        updated_at: "2025-10-31T09:00:00",
        release_date: "2023-11-01T00:00:00",
        thoiGianBaoHanh: 12,
        colors: ["Đen", "Vàng đồng", "Cam nâu"],
        versions: ["8GB/256GB"],
        slides: [
            "assert/img/product/honorx9c_behind.jpg",
            "assert/img/product/honorx9c_after.jpg",
            "assert/img/product/honorx9c_camera.jpg"
        ],
        specs: {
            screen: "AMOLED 6.78 inch, 120Hz",
            chip: "Snapdragon 6 Gen 1",
            ram: "8 GB",
            storage: "256 GB",
            camera: "Chính 108 MP & Phụ 5 MP, 2 MP, 16 MP",
            battery: "5800 mAh, sạc nhanh 35W",
            os: "Android 13, MagicUI 7.2",
            sim: "2 SIM (Nano SIM)",
            madeIn: "Trung Quốc"
        }
    }
];

export const product_variants = [
    { id: 1, product_id: 1, sku: "IP15PM-256GB-Titan", color_name: "Titan Tự Nhiên", storage_capacity: "256GB", variant_price: 29990000, quantity: 20, status: 1 },
    { id: 2, product_id: 1, sku: "IP15PM-256GB-Blue", color_name: "Xanh Titan", storage_capacity: "256GB", variant_price: 29990000, quantity: 15, status: 1 },
    { id: 3, product_id: 2, sku: "IP14-128GB-Red", color_name: "Đỏ", storage_capacity: "128GB", variant_price: 17990000, quantity: 25, status: 1 },
    { id: 4, product_id: 2, sku: "IP14-128GB-Black", color_name: "Đen", storage_capacity: "128GB", variant_price: 17990000, quantity: 30, status: 1 },
    { id: 5, product_id: 3, sku: "VIVO-V30-256GB-Blue", color_name: "Xanh Ngọc", storage_capacity: "256GB", variant_price: 12990000, quantity: 20, status: 1 },
    { id: 6, product_id: 3, sku: "VIVO-V30-256GB-Black", color_name: "Đen", storage_capacity: "256GB", variant_price: 12990000, quantity: 15, status: 1 },
    { id: 7, product_id: 4, sku: "VIVO-Y100-128GB-Purple", color_name: "Tím", storage_capacity: "128GB", variant_price: 7990000, quantity: 18, status: 1 },
    { id: 8, product_id: 4, sku: "VIVO-Y100-128GB-Green", color_name: "Xanh Lục", storage_capacity: "128GB", variant_price: 7990000, quantity: 22, status: 1 },
    { id: 9, product_id: 5, sku: "REALME-12PRO+-256GB-Gold", color_name: "Vàng", storage_capacity: "256GB", variant_price: 14990000, quantity: 12, status: 1 },
    { id: 10, product_id: 6, sku: "REALME-C67-128GB-Black", color_name: "Đen", storage_capacity: "128GB", variant_price: 6490000, quantity: 30, status: 1 },
    { id: 11, product_id: 7, sku: "SONY-1V-256GB-Black", color_name: "Đen", storage_capacity: "256GB", variant_price: 27990000, quantity: 10, status: 1 },
    { id: 12, product_id: 8, sku: "SONY-5V-128GB-Silver", color_name: "Bạc", storage_capacity: "128GB", variant_price: 21990000, quantity: 8, status: 1 },
    { id: 13, product_id: 9, sku: "HONOR-200L-256GB-Blue", color_name: "Xanh", storage_capacity: "256GB", variant_price: 8990000, quantity: 16, status: 1 },
    { id: 14, product_id: 10, sku: "HONOR-X9B-256GB-Orange", color_name: "Cam", storage_capacity: "256GB", variant_price: 8990000, quantity: 20, status: 1 }
];

export const vouchers = [
    {
        id: 1,
        voucher_code: "GIAM5",
        discount_amount: 5,
        type: 1,
        status: 1,
        min_order_value: 5000000,
        start_date: "2025-10-01T00:00:00",
        end_date: "2025-12-31T23:59:59",
        created_at: "2025-10-31T09:00:00"
    },
    {
        id: 2,
        voucher_code: "FREESHIP",
        discount_amount: 50000,
        type: 2,
        status: 1,
        min_order_value: 1000000,
        start_date: "2025-10-01T00:00:00",
        end_date: "2025-12-31T23:59:59",
        created_at: "2025-10-31T09:00:00"
    }
];
